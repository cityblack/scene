package com.lzh.game.scene.core;

import com.lzh.game.scene.common.connect.server.ConnectServer;
import com.lzh.game.scene.common.connect.server.MethodInvokeHelper;
import com.lzh.game.scene.common.connect.server.SimpleInvokeHelper;
import com.lzh.game.scene.core.controller.NodeController;
import com.lzh.game.scene.core.controller.SceneController;
import com.lzh.game.scene.core.jrfa.ReplicatorCmd;
import com.lzh.game.scene.core.jrfa.process.SceneInstanceProcess;
import com.lzh.game.scene.core.node.NodeService;
import com.lzh.game.scene.core.node.RedisNodeServiceImpl;
import com.lzh.game.scene.core.service.JRafClusterServer;
import com.lzh.game.scene.core.service.RedisClusterServer;
import com.lzh.game.scene.core.service.SceneService;
import com.lzh.game.scene.core.service.impl.CpSceneServiceImpl;
import com.lzh.game.scene.core.service.impl.RedisSceneServiceImpl;
import com.lzh.game.scene.core.service.impl.SceneInstanceManageImpl;
import com.lzh.game.scene.core.service.impl.mode.InstanceSubscribe;
import com.lzh.game.scene.core.service.impl.mode.InstanceSubscribeListener;

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
        RedisClusterServer<ClusterServerConfig> server = new RedisClusterServer<>(config);
//        JRafClusterServer<ClusterServerConfig> server = new JRafClusterServer<>(config);
        server.init();
        InstanceSubscribe subscribe = new InstanceSubscribeListener(server);
//        final SceneInstanceManageImpl manage = new SceneInstanceManageImpl();
//        SceneService service = new CpSceneServiceImpl(manage, server);
        SceneService service = new RedisSceneServiceImpl(server.getClient(), subscribe);
        SceneController sceneController = new SceneController();
        sceneController.setSceneService(service);

        NodeService nodeService = new RedisNodeServiceImpl(server, server.getClient());
        NodeController nodeController = new NodeController();
        nodeController.setNodeService(nodeService);

        server.addCmdTarget(Arrays.asList(sceneController, nodeController));
//        SceneInstanceProcess sceneInstanceProcess = new SceneInstanceProcess(manage);
//        server.getJrService().addRequestProcess(ReplicatorCmd.REGISTER_SCENE, sceneInstanceProcess);

        server.start();
        return server;
    }
}
