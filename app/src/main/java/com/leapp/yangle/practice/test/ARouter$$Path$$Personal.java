package com.leapp.yangle.practice.test;

import com.leapp.yangle.arouter.annotations.model.RouterBean;
import com.leapp.yangle.arouter.api.ARouterLoadPath;
import com.leapp.yangle.module.personal.Personal_MainActivity;

import java.util.HashMap;
import java.util.Map;


public class ARouter$$Path$$Personal implements ARouterLoadPath {
    @Override
    public Map<String, RouterBean> loadPath() {
        Map<String, RouterBean> pathMap = new HashMap<>();
        pathMap.put("/personal/Personal_MainActivity",
                RouterBean.create(RouterBean.Type.ACTIVITY,
                        Personal_MainActivity.class,
                        "/personal/Personal_MainActivity", "personal"));
        return pathMap;
    }
}
