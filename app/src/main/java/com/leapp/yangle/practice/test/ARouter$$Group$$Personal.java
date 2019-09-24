package com.leapp.yangle.practice.test;

import com.leapp.yangle.arouter.api.core.ARouterLoadGroup;
import com.leapp.yangle.arouter.api.core.ARouterLoadPath;

import java.util.HashMap;
import java.util.Map;

public class ARouter$$Group$$Personal implements ARouterLoadGroup {
    @Override
    public Map<String, Class<? extends ARouterLoadPath>> loadGroup() {
        Map<String, Class<? extends ARouterLoadPath>> groupMap = new HashMap<>();
        groupMap.put("personal", ARouter$$Path$$Personal.class);
        return groupMap;
    }
}
