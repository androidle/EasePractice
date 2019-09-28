package com.leapp.yangle.arouter.annotations.model;

import javax.lang.model.element.Element;

public class RouterBean {

    private RouterBean() {
    }

    private RouterBean(Builder builder) {
        this.element = builder.element;
        this.group = builder.group;
        this.path = builder.path;
    }

    private RouterBean(Type type, Class clazz, String path, String group) {
        this.type = type;
        this.clazz = clazz;
        this.group = group;
        this.path = path;
    }

    public static RouterBean create(Type type, Class clazz,String path,String group) {
        return new RouterBean(type, clazz, path, group);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Element getElement() {
        return element;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getGroup() {
        return group;
    }

    public String getPath() {
        return path;
    }

    public enum Type{
        ACTIVITY,
        CALL
    }

    private Type type;

    private Element element;

    private Class<?> clazz;

    private String group;

    private String path;

    public static final class Builder{

        private Element element;

        private String group;

        private String path;

        public Builder setElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public RouterBean build() {
            if (path == null || path.length() == 0) {
                throw new IllegalArgumentException("path cannot be empty ,eg /app/MainActivity");
            }

            return new RouterBean(this);
        }
    }

    @Override
    public String toString() {
        return "RouterBean{" +
                "type=" + type +
                ", element=" + element +
                ", clazz=" + clazz +
                ", group='" + group + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
