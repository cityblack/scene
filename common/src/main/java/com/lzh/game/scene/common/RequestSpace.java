package com.lzh.game.scene.common;

public interface RequestSpace {

    int INSTANCE_SPACE = 1000;
    int INSTANCE_REGISTER = 1;
    int INSTANCE_GET = 2;
    int INSTANCE_SUBSCRIBE = 3;

    int LISTEN_INSTANCE_SPACE = 2000;
    int LISTEN_INSTANCE_CHANGE = 1;
    int LISTEN_NODE_CHANGE = 2;

    int NODE_SPACE = 3000;
    int NODE_REGISTER = 1;


    static int cmd(int space, int target) {
        return space + target;
    }
}
