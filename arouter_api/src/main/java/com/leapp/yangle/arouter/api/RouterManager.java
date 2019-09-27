package com.leapp.yangle.arouter.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import com.leapp.yangle.arouter.annotations.model.RouterBean;
import com.leapp.yangle.arouter.api.core.ARouterLoadGroup;
import com.leapp.yangle.arouter.api.core.ARouterLoadPath;

public class RouterManager {

    private String group;
    private String path;
    private static RouterManager instance;

    private LruCache<String, ARouterLoadGroup> groupLruCache;
    private LruCache<String, ARouterLoadPath> pathLruCache;

    private static final String GROUP_FILE_PREFIX_NAME = "ARouter$$Group$$";

    private BundlerManager bundlerManager;

    public static RouterManager getInstance() {
        if (instance == null) {
            synchronized (RouterManager.class) {
                if (instance == null) {
                    instance = new RouterManager();
                }
            }
        }

        return instance;
    }

    private RouterManager() {
        groupLruCache = new LruCache<>(100);
        pathLruCache = new LruCache<>(100);
    }

    public BundlerManager build(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new IllegalArgumentException("invalid path eg /app/MainActivity");
        }

        this.group = subFromPath(path);
        this.path = path;

        return bundlerManager =  new BundlerManager();
    }

    private String subFromPath(String path) {
        if (path.lastIndexOf("/") == 0) {
            throw new IllegalArgumentException("invalid path eg /app/MainActivity");
        }

        String finalGroup = path.substring(1, path.indexOf("/", 1));
        if (TextUtils.isEmpty(finalGroup)) {
            throw new IllegalArgumentException("invalid path eg /app/MainActivity");
        }
        return finalGroup;
    }

    public Object navigation(Context context, int code) {
        String groupClassName = context.getPackageName() + ".apt." + GROUP_FILE_PREFIX_NAME + group;
        Log.e("===groupClassName=>>>", "==== " + groupClassName);
        try {
            ARouterLoadGroup groupLoad = groupLruCache.get(groupClassName);
            if (groupLoad == null) {
                Class<?> groupCazz = Class.forName(groupClassName);
                groupLoad = (ARouterLoadGroup) groupCazz.newInstance();
                groupLruCache.put(groupClassName, groupLoad);
            }

            if (groupLoad.loadGroup().isEmpty()) {
                throw new RuntimeException("路由表加载失败!!!");
            }

            ARouterLoadPath pathLoad = pathLruCache.get(path);
            if (pathLoad == null ) {
                Class<? extends ARouterLoadPath> pathLoadClazz = groupLoad.loadGroup().get(group);
                if (pathLoadClazz != null) pathLoad = pathLoadClazz.newInstance();
                if (pathLoad != null) pathLruCache.put(path, pathLoad);

            }
            if (pathLoad != null) {
                if (pathLoad.loadPath().isEmpty()) {
                    throw new RuntimeException("路由表加载失败!!!");
                }
            }

            RouterBean routerBean = pathLoad.loadPath().get(path);
            if (routerBean != null) {
                switch (routerBean.getType()) {
                    case ACTIVITY:
                        Intent intent = new Intent(context, routerBean.getClazz());
                        intent.putExtras(bundlerManager.getBundle());

                        if (bundlerManager.isResult()) {
                            ((Activity) context).setResult(code, intent);
                            ((Activity) context).finish();
                        }

                        if (code > 0) {
                            ((Activity) context).startActivityForResult(intent,code,bundlerManager.getBundle());
                        } else {
                            context.startActivity(intent,bundlerManager.getBundle());
                        }
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
