package com.lzh.game.scene.common.connect;

import java.util.Collection;

public interface ConnectManage<T extends Connect> {

    T getConnect(String key);

    Collection<T> getAllConnect();

    void shutdown();

    T putConnect(String key, T connect);

    default String toAddress(String host, int port) {
        return host + ":" + port;
    }

    T removeConnect(String key);

    boolean contain(String key);
}
