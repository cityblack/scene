package com.lzh.game.scene.common.connect;

import com.lzh.game.scene.common.connect.scene.SceneConnectManage;

public interface ConnectServer extends SceneConnectManage {

    int port();

    void start();

    ServerConfig config();

}
