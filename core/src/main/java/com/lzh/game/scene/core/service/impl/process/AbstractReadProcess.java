package com.lzh.game.scene.core.service.impl.process;

import com.lzh.game.scene.core.service.ReplicatorCmd;
import com.lzh.game.scene.core.service.impl.AbstractExchangeProcess;

import java.io.Serializable;

/**
 * 读处理
 * @param <R> -- 请求参数类型
 * @param <P> -- 返回参数类型
 */
public abstract class AbstractReadProcess<R extends Serializable, P extends Serializable>
        extends AbstractExchangeProcess<R, P> {

    @Override
    public P onRequest(ReplicatorCmd cmd, R data) {
        return null;
    }

    public abstract Class<P> getResponseType();

}
