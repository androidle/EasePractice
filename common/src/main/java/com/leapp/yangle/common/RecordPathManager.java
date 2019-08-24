package com.leapp.yangle.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordPathManager {

    private static Map<String, List<PathBean>> group = new HashMap();

    /**
     *
     * @param groupName
     * @param path
     * @param clazz
     */
    public static void joinGroup(String groupName, String path, Class clazz) {
        List<PathBean> list = group.get(groupName);
        if (list == null) {
            list = new ArrayList<>();
            list.add(new PathBean(path, clazz));
            group.put(groupName, list);
        } else {
            for (PathBean pathBean : list) {
                if (!pathBean.getPath().equals(path)) {
                    list.add(new PathBean(path, clazz));
                    group.put(groupName, list);
                }
            }
        }
    }

    /**
     *
     * @param groupName
     * @param pathName
     * @return Class
     */
    public static Class<?> getTargetClass(String groupName, String pathName) {
        List<PathBean> list = group.get(groupName);
        if (list == null) return null;
        for (PathBean pathBean : list) {
            if (pathName.equalsIgnoreCase(pathBean.getPath())) {
                return pathBean.getClazz();
            }
        }

        return null;
    }
}
