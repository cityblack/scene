package com.lzh.game.scene.example;

import com.lzh.game.scene.api.AsyncSceneApi;
import com.lzh.game.scene.api.AsyncSceneApiImpl;
import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.sofa.ApiClient;
import com.lzh.game.scene.api.connect.sofa.SofaConnectClient;
import com.lzh.game.scene.api.option.ListenController;
import com.lzh.game.scene.api.server.SceneService;
import com.lzh.game.scene.api.server.SceneServiceImpl;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ClientServerDemo {

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

        /*final AsyncSceneApi api = new AsyncSceneApiImpl(client, sceneService);
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
        });*/
    }
}
