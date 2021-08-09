package com.lzh.game.scene.core.jrfa.process;

import com.lzh.game.scene.core.jrfa.ReplicatorCmd;
import com.lzh.game.scene.core.jrfa.AbstractExchangeProcess;

import java.io.Serializable;

/**
 * 写入处理
 * @param <T> -- 请求参数类型
 */
public abstract class AbstractWriteProcess<T extends Serializable>
        extends AbstractExchangeProcess<T, Void> {

    @Override
    public Void onRequest(ReplicatorCmd cmd, T data) {
        doRequest(cmd, data);
        return null;
    }

    public abstract void doRequest(ReplicatorCmd cmd, T data);
}
