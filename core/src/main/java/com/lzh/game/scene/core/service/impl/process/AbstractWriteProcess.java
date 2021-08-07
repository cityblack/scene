package com.lzh.game.scene.core.service.impl.process;

import com.lzh.game.scene.core.service.ReplicatorCmd;
import com.lzh.game.scene.core.service.impl.AbstractExchangeProcess;

import java.io.Serializable;

/**
 * 写入处理
 * @param <T> -- 请求参数类型
 */
public abstract class AbstractWriteProcess<T extends Serializable>
        extends AbstractExchangeProcess<T, Void> {

    public AbstractWriteProcess(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public Void onRequest(ReplicatorCmd cmd, T data) {
        doRequest(cmd, data);
        return null;
    }

    public abstract void doRequest(ReplicatorCmd cmd, T data);
}
