package com.hawk.agent.jassist.util;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static String toDotClass(String className) {
        if (!isEmpty(className)) {
            return className.replace("/", ".");
        }
        return className;
    }
}
