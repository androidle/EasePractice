package com.leapp.yangle.practice.test;

import com.leapp.yangle.arouter.annotations.model.RouterBean;
import com.leapp.yangle.arouter.api.ARouterLoadPath;
import com.leapp.yangle.practice.MainActivity;

import java.util.HashMap;
import java.util.Map;


public class ARouter$$Path$$App implements ARouterLoadPath {
    @Override
    public Map<String, RouterBean> loadPath() {
        Map<String, RouterBean> pathMap = new HashMap<>();
        pathMap.put("/app/MainActivity",
                RouterBean.create(RouterBean.Type.ACTIVITY,
                        MainActivity.class,
                        "/app/MainActivity", "app"));
        return pathMap;
    }
}
