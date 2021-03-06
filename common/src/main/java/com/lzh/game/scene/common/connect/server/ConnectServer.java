package com.lzh.game.scene.common.connect.server;

/**
 * 因为使用的时候需要双工通信，市面上的rpc都是单向的，所以重写了一套简单的双工请求通信
 */
public interface ConnectServer<T extends ServerConfig> extends Bootstrap<T> {

    int port();

    RequestHandler requestHandler();

    InvokeManage invokeManage();
}
