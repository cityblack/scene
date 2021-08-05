package com.lzh.game.scene.api;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.api.connect.sofa.SofaConnectClient;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class AppTest {

    @Test
    public void start() {
        ApiConfig config = new ApiConfig();
        Member member = new Member();
        member.setHost("127.0.0.1");
        member.setPort(8181);
        config.addMember(member);

        ConnectClient client = new SofaConnectClient(config);
        client.start();

        Connect connect = client.getConnect(member.getHost(), member.getPort(), NodeType.SCENE_NODE);
        Request request = Request.of(10086);
        request.setParam("hell world!!");
        connect.sendOneWay(request);

        CompletableFuture<Response> future = connect.sendMessage(request);
        future.thenAccept(response -> System.out.println(response.getParam()));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        AsyncSceneApi api = new AsyncSceneApiImpl();
//        String group = "test";
//
//        api.subscribe(group, SceneChangeStatus.CHANGE, instance -> System.out.println(instance));
//
//        api.registerSceneInstance(group, new Instance());
    }
}
