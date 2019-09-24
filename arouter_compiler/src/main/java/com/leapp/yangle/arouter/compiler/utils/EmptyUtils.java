package com.leapp.yangle.arouter.compiler.utils;

import java.util.Collection;
import java.util.Map;

public class EmptyUtils {

    public static boolean isEmpty(Map<?, ?> options) {
        return options == null || options.isEmpty();
    }

    public static boolean isEmpty(CharSequence content) {
        return content == null || content.length() == 0;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
