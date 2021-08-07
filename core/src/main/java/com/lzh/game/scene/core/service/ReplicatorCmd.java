package com.lzh.game.scene.core.service;

import com.lzh.game.scene.core.service.impl.AbstractExchangeProcess;
import com.lzh.game.scene.core.service.impl.process.SceneInstanceProcess;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum ReplicatorCmd {
    // 注册场景
    REGISTER_SCENE(1, SceneInstanceProcess.class),

    ;
    private int cmd;
    // 实现的类型 一对多
    private Class<? extends AbstractExchangeProcess> processClass;

    private final static Map<Integer, ReplicatorCmd> CACHE = new HashMap<>(values().length << 1);

    static {
        for (ReplicatorCmd replicatorCmd : values()) {
            CACHE.put(replicatorCmd.getCmd(), replicatorCmd);
        }
    }

    ReplicatorCmd(int cmd, Class<? extends AbstractExchangeProcess> processClass) {
        this.cmd = cmd;
        this.processClass = processClass;
    }

    public int getCmd() {
        return cmd;
    }

    public <T extends Serializable, R extends Object> AbstractExchangeProcess<T, R> getProcess() {
        return AbstractExchangeProcess.findProcess(this.processClass);
    }

    public static ReplicatorCmd of(int cmd) {
        return CACHE.get(cmd);
    }

}
