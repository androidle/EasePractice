package com.leapp.yangle.practice.test;

import com.leapp.yangle.arouter.api.ARouterLoadGroup;
import com.leapp.yangle.arouter.api.ARouterLoadPath;

import java.util.HashMap;
import java.util.Map;

public class ARouter$$Group$$App implements ARouterLoadGroup {
    @Override
    public Map<String, Class<? extends ARouterLoadPath>> loadGroup() {
        Map<String, Class<? extends ARouterLoadPath>> groupMap = new HashMap<>();
        groupMap.put("app", ARouter$$Path$$App.class);
        return groupMap;
    }
}
