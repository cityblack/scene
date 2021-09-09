package com.lzh.game.scene.example;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.sofa.ApiClient;
import com.lzh.game.scene.api.controller.SceneController;
import com.lzh.game.scene.api.scene.CreateSceneProcess;
import com.lzh.game.scene.api.scene.SceneNodeBootstrap;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.server.ServerConfig;
import com.lzh.game.scene.common.connect.sofa.SofaServer;

import java.util.Arrays;

public class SceneNodeDemo {

    public static void main(String[] args) {
        start(8888);
        start(9999);
    }

    public static void start(int port) {
        ApiConfig config = new ApiConfig();
        config.setPort(port);
        config.setNodeType(NodeType.SCENE_NODE.ordinal());

        Member member = new Member();
        member.setHost("127.0.0.1");
        member.setPort(8081);
        config.addMember(member);

        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setPort(config.getPort());
        SofaServer<ServerConfig> sofaServer = new SofaServer<>(serverConfig);
        sofaServer.startup();

        ApiClient client = new ApiClient(config);
        client.startup();

        SceneNodeBootstrap bootstrap = new SceneNodeBootstrap();
        bootstrap.setClient(client);
        bootstrap.setProcess(newProcess());
        SceneController controller = new SceneController();
        controller.setBootstrap(bootstrap);

        client.addCmdTarget(Arrays.asList(controller));
    }

    public static CreateSceneProcess newProcess() {
        return (map -> {
            SceneInstance instance = new SceneInstance();
            instance.setUnique("10086-" + map);
            instance.setMap(map);
            return instance;
        });
    }
}
