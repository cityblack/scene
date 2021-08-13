package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.scene.SceneConnectManage;

/**
 * 因为使用的时候需要双工通信，市面上的rpc都是单向的，所以重写了一套简单的双工请求通信
 */
public interface ConnectServer<T extends ServerConfig> extends Bootstrap<T> {

    int port();
    /**
     * 连接管理器
     * @return
     */
    SceneConnectManage manage();

    RequestHandler requestHandler();

    InvokeManage invokeManage();
}
