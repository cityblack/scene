package com.lzh.game.scene.api;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

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

        api.registerSceneInstance(group, new Instance());

    }

    public static class Instance implements SceneInstance {

        @Override
        public String group() {
            return null;
        }

        @Override
        public String unique() {
            return null;
        }

        @Override
        public int map() {
            return 0;
        }
    }
}
