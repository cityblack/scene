package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.core.service.ReplicatorCmd;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractWriteProcess<T extends Serializable> {

    public static final Map<Class<?>, AbstractWriteProcess> SINGLE = new ConcurrentHashMap<>();

    public static void addProcess(Class<?> clazz, AbstractWriteProcess process) {
        SINGLE.put(clazz, process);
    }

    public static AbstractWriteProcess findProcess(Class<? extends AbstractWriteProcess> clazz) {
        return SINGLE.get(clazz);
    }

    private Class<T> clazz;

    public AbstractWriteProcess(Class<T> clazz) {
        this.clazz = clazz;
        addProcess(this.getClass(),this);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public abstract void onRequest(ReplicatorCmd cmd, T data);
}
