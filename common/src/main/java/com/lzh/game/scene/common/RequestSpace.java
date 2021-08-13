package com.lzh.game.scene.common;

public class RequestSpace {

    public static final int INSTANCE_SPACE = 1000;

    public static final int NODE_SPACE = 2000;

    public static int cmd(int space, int target) {
        return space + target;
    }
}
