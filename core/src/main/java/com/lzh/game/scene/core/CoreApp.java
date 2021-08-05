package com.lzh.game.scene.core;

import com.lzh.game.scene.common.connect.server.ConnectServer;
import com.lzh.game.scene.core.service.SofaClusterServer;

public class CoreApp {

    public static void main(String[] args) {
        ClusterServerConfig config = new ClusterServerConfig();
        config.getCluster().add("localhost:8081");
        ConnectServer<ClusterServerConfig> server = new SofaClusterServer<>(config);
        server.start();
    }
}
