package com.lzh.game.scene.core;

import com.lzh.game.scene.common.connect.server.ConnectServer;
import com.lzh.game.scene.common.connect.server.MethodInvokeFactory;
import com.lzh.game.scene.common.connect.server.SimpleInvokeFactory;
import com.lzh.game.scene.core.controller.SceneController;
import com.lzh.game.scene.core.jrfa.ReplicatorCmd;
import com.lzh.game.scene.core.jrfa.process.SceneInstanceProcess;
import com.lzh.game.scene.core.service.JRafClusterServer;
import com.lzh.game.scene.core.service.SceneService;
import com.lzh.game.scene.core.service.impl.CpSceneServiceImpl;
import com.lzh.game.scene.core.service.impl.SceneInstanceManageImpl;

import java.io.File;
import java.util.Arrays;

public class Star {

    public static void run(String[] args) {
        ClusterServerConfig config = new ClusterServerConfig();
        config.getCluster().add("localhost:8081");
        config.getCluster().add("localhost:8082");
        config.getCluster().add("localhost:8083");

        for (int i = 0; i < config.getCluster().size(); i++) {
            config.setPort(8081 + i);
            config.setConsistLogUri("classpath:logs" + File.separator + config.getPort());
            config.setMetaUri("classpath:scene" + File.separator + config.getPort());
            config.setSnapshotUri("classpath:snapshot" + File.separator + config.getPort());
            buildJRaftClusterServer(config);
        }
    }

    private static ConnectServer buildJRaftClusterServer(ClusterServerConfig config) {
        JRafClusterServer<ClusterServerConfig> server = new JRafClusterServer<>(config);
        server.init();

        SceneService service = new CpSceneServiceImpl(new SceneInstanceManageImpl(), server.getJrService().replicator());
        SceneController sceneController = new SceneController();
        sceneController.setSceneService(service);

        MethodInvokeFactory methodInvokeFactory = new SimpleInvokeFactory(server.getRequestHelper()
                , Arrays.asList(sceneController));
        server.setMethodInvokeFactory(methodInvokeFactory);

        final SceneInstanceManageImpl manage = new SceneInstanceManageImpl();
        SceneInstanceProcess sceneInstanceProcess = new SceneInstanceProcess(manage);
        server.getJrService().addRequestProcess(ReplicatorCmd.REGISTER_SCENE, sceneInstanceProcess);

        server.start();
        return server;
    }
}
