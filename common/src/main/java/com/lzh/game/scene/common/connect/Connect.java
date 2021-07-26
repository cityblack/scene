package com.lzh.game.scene.common.connect;

/**
 * 服务端和客户端都持有
 */
public interface Connect {

    Response sendMessage(Request request);

    long reflectCount();

    Object getAttr(String key);

    void setAttr(String key, Object o);

    /**
     * 唯一连接
     * @return
     */
    String key();
}
