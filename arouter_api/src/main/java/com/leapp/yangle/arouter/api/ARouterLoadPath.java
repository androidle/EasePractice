package com.leapp.yangle.arouter.api;

import com.leapp.yangle.arouter.annotations.model.RouterBean;

import java.util.Map;

public interface ARouterLoadPath {


    /**
     * 加截路由group中的详细path数据
     * @return 如 key="/app/MainActivity" value = MainActivity.class
     * 封装到RouterBean中
     */
    Map<String, RouterBean> loadPath();
}
