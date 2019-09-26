package com.leapp.yangle.arouter.api;

import android.app.Activity;
import android.util.LruCache;

import androidx.annotation.NonNull;

import com.leapp.yangle.arouter.api.core.ParameterLoad;

public class ParameterManager {


    private static ParameterManager instance;

    // key:类名,value:参数Parameter加载接口
    private LruCache<String, ParameterLoad> cache;

    private static final String FILE_SUFFIX_NAME = "$$Parameter";

    public static ParameterManager getInstance() {
        if (instance == null) {
            synchronized (ParameterManager.class) {
                if (instance == null) {
                    instance = new ParameterManager();
                }
            }
        }

        return instance;
    }

    private ParameterManager() {
        cache = new LruCache<>(100);
    }

    public void loadParameter(@NonNull Activity activity) {
        String className = activity.getClass().getName();
        ParameterLoad parameterLoad = cache.get(className);

        try {
            if (parameterLoad == null) {
                Class<?> clazz = Class.forName(className + FILE_SUFFIX_NAME);
                parameterLoad = (ParameterLoad) clazz.newInstance();
                cache.put(className, parameterLoad);
            }

            parameterLoad.loadParameter(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

