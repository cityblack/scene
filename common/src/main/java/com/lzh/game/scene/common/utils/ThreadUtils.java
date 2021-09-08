package com.lzh.game.scene.common.utils;

import com.alipay.remoting.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

    public static ExecutorService createSingleService(String name) {
        return Executors.newSingleThreadExecutor(new NamedThreadFactory(name));
    }

    public static ExecutorService createFixedService(int core, String name) {
        return Executors.newFixedThreadPool(core, new NamedThreadFactory(name));
    }

    public static ExecutorService createCpuFixedService(String name) {
        return createFixedService(Runtime.getRuntime().availableProcessors(), name);
    }

    public static ExecutorService createIoFixedService(String name) {
        return createFixedService(Runtime.getRuntime().availableProcessors() * 2, name);
    }
}
