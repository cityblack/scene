package com.lzh.game.scene.core.jrfa;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractExchangeProcess<T extends Serializable, E extends Object> {

    public static final Map<Class<? extends AbstractExchangeProcess>, AbstractExchangeProcess> SINGLE = new ConcurrentHashMap<>();

    public static void addProcess(Class<? extends AbstractExchangeProcess> clazz, AbstractExchangeProcess process) {
        SINGLE.put(clazz, process);
    }

    public static AbstractExchangeProcess findProcess(Class<? extends AbstractExchangeProcess> clazz) {
        return SINGLE.get(clazz);
    }

    public AbstractExchangeProcess() {
    }

    public abstract E onRequest(ReplicatorCmd cmd, T data);

    public abstract Class<T> getRequestParamType();
}
