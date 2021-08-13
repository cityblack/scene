package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.BootstrapConfig;

public interface Bootstrap<T extends BootstrapConfig> {

    void init();

    void start();

    void shutdown();

    default void startup() {
        this.init();
        this.start();
    }

    T getConfig();
}
