package com.lzh.game.scene.common.connect;

/**
 * 通信的Connect
 */
public interface ConnectFactory {

    Connect createConnect(String address, Object param);

    default Connect createConnect(String address) {
        return createConnect(address, null);
    }
}
