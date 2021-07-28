package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;

public interface ConnectServer {

    int port();

    void start();

    void shutdown();

    ServerConfig config();

    /**
     * 连接管理器
     * @return
     */
    SceneConnectManage<SceneConnect> manage();

}
