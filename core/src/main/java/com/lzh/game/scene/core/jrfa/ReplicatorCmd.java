package com.lzh.game.scene.core.jrfa;

import com.lzh.game.scene.core.jrfa.process.SceneInstanceProcess;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum ReplicatorCmd {
    // 注册场景
    REGISTER_SCENE(1),

    ;
    private int cmd;

    private final static Map<Integer, ReplicatorCmd> CACHE = new HashMap<>(values().length << 1);

    static {
        for (ReplicatorCmd replicatorCmd : values()) {
            CACHE.put(replicatorCmd.getCmd(), replicatorCmd);
        }
    }

    ReplicatorCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getCmd() {
        return cmd;
    }

    public static ReplicatorCmd of(int cmd) {
        return CACHE.get(cmd);
    }
}
