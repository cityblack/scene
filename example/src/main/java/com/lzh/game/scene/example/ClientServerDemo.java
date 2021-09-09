package com.lzh.game.scene.example;

import com.lzh.game.scene.api.AsyncSceneApi;
import com.lzh.game.scene.api.AsyncSceneApiImpl;
import com.lzh.game.scene.api.scene.transport.TransportSceneData;
import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.sofa.ApiClient;
import com.lzh.game.scene.api.controller.ListenController;
import com.lzh.game.scene.api.scene.SceneService;
import com.lzh.game.scene.api.scene.impl.SceneServiceImpl;

import java.util.Arrays;

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

        final AsyncSceneApi api = new AsyncSceneApiImpl(client, sceneService);
        api.createScene("10086", 1, 0);
//        enterCopy(api);
        /*String group = "group";
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

    private static void enterCopy(AsyncSceneApi api) {
        api.createScene("123", 1, 0, instance -> {
            TransportSceneData<Long> sceneData = new TransportSceneData<>(1, 10086L);
//            api.transportScene(instance.getGroup(), instance.getUnique(), sceneData);
        });
    }
}
