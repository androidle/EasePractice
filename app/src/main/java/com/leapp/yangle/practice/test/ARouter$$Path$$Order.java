package com.leapp.yangle.practice.test;

import com.leapp.yangle.arouter.annotations.model.RouterBean;
import com.leapp.yangle.arouter.api.ARouterLoadPath;
import com.leapp.yangle.module.order.Order_MainActivity;

import java.util.HashMap;
import java.util.Map;


public class ARouter$$Path$$Order implements ARouterLoadPath {
    @Override
    public Map<String, RouterBean> loadPath() {
        Map<String, RouterBean> pathMap = new HashMap<>();
        pathMap.put("/order/Order_MainActivity",
                RouterBean.create(RouterBean.Type.ACTIVITY,
                        Order_MainActivity.class,
                        "/order/Order_MainActivity", "order"));
        return pathMap;
    }
}
