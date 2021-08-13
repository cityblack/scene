package com.lzh.game.scene.api;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.api.connect.sofa.SofaConnectClient;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

public class App {

    public static void main(String[] args) {
        ApiConfig config = new ApiConfig();
        config.setNodeType(1);

        Member member = new Member();
        member.setHost("127.0.0.1");
        member.setPort(8081);
        config.addMember(member);
        ConnectClient client = new SofaConnectClient(config);
        client.startup();
        AsyncSceneApi api = new AsyncSceneApiImpl(client);
        String group = "group";
        SceneInstance instance = new SceneInstance();
        instance.setGroup(group);
        instance.setMap(1);
        instance.setUnique("group-1-1");
        api.subscribe(group, SceneChangeStatus.CHANGE, System.out::println);
        api.registerSceneInstance(group, instance);

    }
}
