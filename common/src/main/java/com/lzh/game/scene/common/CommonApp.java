package com.lzh.game.scene.common;

import com.lzh.game.scene.common.connect.server.ConnectServer;
import com.lzh.game.scene.common.connect.server.ServerConfig;
import com.lzh.game.scene.common.connect.sofa.SofaServer;

public class CommonApp {

    public static void main(String[] args) {
        ServerConfig config = new ServerConfig();
        config.setPort(8181);
        ConnectServer server = new SofaServer(config);
        server.start();
    }
}
