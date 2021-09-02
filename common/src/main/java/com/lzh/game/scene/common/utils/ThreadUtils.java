package com.lzh.game.scene.common.utils;

import com.alipay.remoting.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

    public ExecutorService createSingleService(String name) {
        return Executors.newSingleThreadExecutor(new NamedThreadFactory(name));
    }

    public ExecutorService createFixedService(int core, String name) {
        return Executors.newFixedThreadPool(core, new NamedThreadFactory(name));
    }
}
