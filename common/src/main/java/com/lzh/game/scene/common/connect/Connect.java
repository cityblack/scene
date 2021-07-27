package com.lzh.game.scene.common.connect;

import java.util.concurrent.CompletableFuture;

/**
 * 服务端和客户端都持有
 */
public interface Connect {

    void sendOneWay(Request request);

    CompletableFuture<Response> sendMessage(Request request);

    long reflectCount();

    Object getAttr(String key);

    void setAttr(String key, Object o);

    /**
     * 唯一连接
     * @return
     */
    String address();
}
