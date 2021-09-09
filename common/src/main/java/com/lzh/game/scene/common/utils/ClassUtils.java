package com.lzh.game.scene.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassUtils {

    private static Map<String, Class<?>> CACHE = new ConcurrentHashMap<>();

    public static Class<?> getClassByName(String className) {
        return CACHE.computeIfAbsent(className, e -> {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException classNotFoundException) {
                return null;
            }
        });
    }

}
