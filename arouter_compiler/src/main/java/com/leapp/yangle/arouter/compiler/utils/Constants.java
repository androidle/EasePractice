package com.leapp.yangle.arouter.compiler.utils;

public class Constants {

    public static final String AROUTER_ANNOTATION_TYPES = "com.leapp.yangle.arouter.annotations.ARouter";

    public static final String PARAMETER_ANNOTATION_TYPES = "com.leapp.yangle.arouter.annotations.Parameter";
    public static final String MODULE_NAME = "moduleName";
    public static final String APT_PACKAGE = "packageNameForApt";
    // string全类名
    public static final String STRING = "java.lang.String";
    // Activity全类名
    public static final String ACTIVITY = "android.app.Activity";

    public static final String BASE_PACKAGE = "com.leapp.yangle.arouter.api";
    // 路由group加载接口
    public static final String AROUTER_GROUP = BASE_PACKAGE + ".core.ARouterLoadGroup";
    // 路由group对应详细加载接口
    public static final String AROUTER_PATH = BASE_PACKAGE + ".core.ARouterLoadPath";

    public static final String PARAMETER_LOAD = BASE_PACKAGE + ".core.ParameterLoad";

    public static final String CALL =  BASE_PACKAGE + ".core.Call";;

    public static final String PATH_METHOD_NAME = "loadPath";

    public static final String GROUP_METHOD_NAME = "loadGroup";

    public static final String PARAMETER_METHOD_NAME = "loadParameter";

    public static final Object PATH_PARAMETER_NAME = "pathMap";

    public static final Object GROUP_PARAMETER_NAME = "groupMap";

    public static final Object PATH_FILE_NAME = "ARouter$$Path$$";

    public static final Object GROUP_FILE_NAME = "ARouter$$Group$$";

    public static final String PARAMETER_NAME = "target";

    public static final String PARAMETER_FILE_NAME = "$$Parameter";


    public static final String ROUTER_MANAGER = "RouterManager";
}
