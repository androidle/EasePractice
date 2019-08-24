package com.leapp.yangle.common;

/**
 * Path object
 * path:"order/Order_MainActivity"
 * clazz:Order_MainActivity.class
 */
public class PathBean {

    private String path;
    private Class clazz;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public PathBean() {
    }

    public PathBean(String path, Class clazz) {
        this.path = path;
        this.clazz = clazz;
    }
}
