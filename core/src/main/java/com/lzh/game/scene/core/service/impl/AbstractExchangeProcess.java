package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.core.service.ReplicatorCmd;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractExchangeProcess<T extends Serializable, E extends Object> {

    public static final Map<Class<?>, AbstractExchangeProcess> SINGLE = new ConcurrentHashMap<>();

    public static void addProcess(Class<?> clazz, AbstractExchangeProcess process) {
        SINGLE.put(clazz, process);
    }

    public static AbstractExchangeProcess findProcess(Class<? extends AbstractExchangeProcess> clazz) {
        return SINGLE.get(clazz);
    }

    private Class<T> clazz;

    public AbstractExchangeProcess(Class<T> clazz) {
        this.clazz = clazz;
        addProcess(this.getClass(),this);
    }

    public Class<T> getRequestParamType() {
        return clazz;
    }

    public abstract E onRequest(ReplicatorCmd cmd, T data);

}
