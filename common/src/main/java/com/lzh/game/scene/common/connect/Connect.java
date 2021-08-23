package com.lzh.game.scene.common.connect;

import java.util.concurrent.CompletableFuture;

/**
 * 服务端和客户端都持有
 */
public interface Connect {

    void sendOneWay(Request request);

    /**
     * 非必要去使用get进行阻塞
     * @param request
     * @return
     */
    <T>CompletableFuture<Response<T>> sendMessage(Request request);

    long reflectCount();

    <T>T getAttr(String key);

    void setAttr(String key, Object o);

    /**
     * 唯一连接
     * @return
     */
    String key();

    String host();

    int port();
}
