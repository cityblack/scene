package com.lzh.game.scene.api;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.sofa.ApiClient;
import com.lzh.game.scene.api.controller.ListenController;
import com.lzh.game.scene.api.scene.SceneService;
import com.lzh.game.scene.api.scene.impl.SceneServiceImpl;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.server.ServerConfig;
import com.lzh.game.scene.common.connect.sofa.SofaServer;

import java.util.Arrays;
import java.util.stream.IntStream;

public class App {

    public static void main(String[] args) {
        ApiConfig config = new ApiConfig();
        config.setNodeType(1);

        Member member = new Member();
        member.setHost("127.0.0.1");
        member.setPort(8081);
        config.addMember(member);

        ApiClient client = new ApiClient(config);
        client.init();

        SceneService sceneService = new SceneServiceImpl();
        ListenController controller = new ListenController();
        controller.setSceneService(sceneService);
        controller.setClientServer(client);

        client.addCmdTarget(Arrays.asList(controller));

        client.start();
        // 场景节点开启服务 连接api端
        if (NodeType.isSceneNode(client.nodeType())) {
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.setPort(config.getPort());
            SofaServer<ServerConfig> sofaServer = new SofaServer<>(serverConfig);
            sofaServer.startup();
        }
        final AsyncSceneApi api = new AsyncSceneApiImpl(client, sceneService);
        String group = "group";
        SceneInstance instance = new SceneInstance();
        instance.setGroup(group);
        instance.setMap(1);
        instance.setUnique("group-1-1");
        api.subscribe(group, SceneChangeStatus.ALL, System.out::println);
        api.registerSceneInstance(group, instance);
        IntStream.range(0, 20).forEach(e -> {
            try {
                Thread.sleep(2000);
                api.getAllSceneInstances(group)
                        .thenAccept(list -> System.out.println("场景数据:" + list))
                        .exceptionally(throwable -> {
                            throwable.printStackTrace();
                            return null;
                        });
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
    }
}
