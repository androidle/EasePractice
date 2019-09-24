package com.leapp.yangle.arouter.api.core;


import java.util.Map;

public interface ARouterLoadGroup {

    /**
     *  加载路由组数据
     *  如: "app" $$ARouter$$Path$$App.class 实现ARouterLoadPath 接口
     * @return
     */
    Map<String, Class<? extends ARouterLoadPath>> loadGroup();
}
