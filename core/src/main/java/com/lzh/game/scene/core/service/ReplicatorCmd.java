package com.lzh.game.scene.core.service;

public enum ReplicatorCmd {

    REGISTER_SCENE(1)

    ;
    private int cmd;

    ReplicatorCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getCmd() {
        return cmd;
    }
}
