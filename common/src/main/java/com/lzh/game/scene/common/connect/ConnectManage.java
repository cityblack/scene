package com.lzh.game.scene.common.connect;

import java.util.Collection;

public interface ConnectManage<T extends Connect> {

    T getConnect(String key);

    Collection<T> getAllConnect();

    void putConnect(String key, T connect);

    boolean removeConnect(String key);

    void shutdown();

    default String toAddress(String host, int port) {
        return host + ":" + port;
    }
}
