package com.lzh.game.scene.common.connect;

import java.util.Collection;

public interface ConnectManage {

    Connect getConnect(String key);

    Collection<Connect> getAllConnect();

    void shutdown();

    Connect putConnect(String key, Connect connect);

    default String toAddress(String host, int port) {
        return host + ":" + port;
    }

    Connect removeConnect(String key);
}
