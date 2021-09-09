package com.lzh.game.scene.common;

public interface RequestSpace {
    // 场景实例相关的
    int INSTANCE_SPACE = 100;
    int INSTANCE_REGISTER = 1;
    int INSTANCE_GET = 2;
    int INSTANCE_SUBSCRIBE = 3;
    int SCENE_TRANSPORT_VERIFY = 4;
    int SCENE_TRANSPORT = 5;
    int SCENE_CREATE = 6;
    int INSTANCE_UNIQUE_GET = 7;

    int LISTEN_INSTANCE_SPACE = 200;
    int LISTEN_INSTANCE_CHANGE = 1;
    int LISTEN_NODE_CHANGE = 2;

    int NODE_SPACE = 300;
    int NODE_REGISTER = 1;
    int NODE_CLIENT_CREATE = 2;

    static int cmd(int space, int target) {
        return space + target;
    }
}
