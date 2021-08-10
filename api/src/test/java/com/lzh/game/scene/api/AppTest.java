package com.lzh.game.scene.api;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.api.connect.sofa.SofaConnectClient;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class AppTest {

    @Test
    public void start() throws InterruptedException {
        ApiConfig config = new ApiConfig();
        Member member = new Member();
        member.setHost("127.0.0.1");
        member.setPort(8081);
        config.addMember(member);

        ConnectClient client = new SofaConnectClient(config);
        client.startup();

        AsyncSceneApi api = new AsyncSceneApiImpl(client);
        SceneInstance instance = new SceneInstance();
        instance.setGroup("10086");
        instance.setUnique("10086-1");
        instance.setMap(10086);
        api.registerSceneInstance(instance.getGroup(), instance);

        Thread.sleep(20000);

//        AsyncSceneApi api = new AsyncSceneApiImpl();
//        String group = "test";
//
//        api.subscribe(group, SceneChangeStatus.CHANGE, instance -> System.out.println(instance));
//
//        api.registerSceneInstance(group, new Instance());
    }
}
