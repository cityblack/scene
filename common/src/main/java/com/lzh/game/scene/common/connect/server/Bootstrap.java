package com.lzh.game.scene.common.connect.server;

public interface Bootstrap {

    void init();

    void start();

    void shutdown();

    default void startup() {
        this.init();
        this.start();
    }
}
