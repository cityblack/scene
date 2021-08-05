package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.core.service.ReplicatorCmd;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractWriteProcess<T extends Serializable> {

    public static Map<ReplicatorCmd, AbstractWriteProcess> PROCESSES = new ConcurrentHashMap<>();

    public static void addProcess(ReplicatorCmd cmd, AbstractWriteProcess process) {
        PROCESSES.put(cmd, process);
    }

    public static AbstractWriteProcess findProcess(ReplicatorCmd cmd) {
        return PROCESSES.get(cmd);
    }

    public static AbstractWriteProcess findProcess(int key) {
        ReplicatorCmd cmd = ReplicatorCmd.of(key);
        return findProcess(cmd);
    }

    public abstract void onRequest(T data);

}
