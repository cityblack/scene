package com.lzh.game.scene.api;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.common.SceneChangeStatus;

public class App {

    public static void main(String[] args) {

        ApiConfig config = new ApiConfig();
        Member member = new Member();
        member.setHost("127.0.0.1");
        member.setPort(8081);
        config.addMember(member);

//        ConnectClient client = new ();
//        client.init(config);

        AsyncSceneApi api = new AsyncSceneApiImpl();
        String group = "test";

        api.subscribe(group, SceneChangeStatus.CHANGE, instance -> System.out.println(instance));


    }
}
