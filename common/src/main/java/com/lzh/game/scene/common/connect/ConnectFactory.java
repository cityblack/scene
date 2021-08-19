package com.lzh.game.scene.common.connect;

/**
 * 通信的Connect
 */
public interface ConnectFactory {

    Connect createConnect(String address, Object... param);
}
