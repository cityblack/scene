package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.*;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.Bootstrap;

import java.util.concurrent.CompletableFuture;


/**
 * 连接管理器，所有的链接，心跳，亚健康处理都由其管理
 */
public interface ConnectClient<C extends Connect> extends Bootstrap<ApiConfig> {
    /**
     * 只创建不持有
     * @param address
     * @param type
     * @return
     */
    C createConnect(String address, NodeType type);

    C createConnect(String host, int port, NodeType type);

    ConnectManage<C> connectManage();

    /**
     * 不存在就创建新的, 并持有
     * @param address
     * @param type
     * @return
     */
    C getConnect(String address, NodeType type);

    C getConnect(String host, int port, NodeType type);

    <T>CompletableFuture<Response<T>> sendMessage(Request request);

    void sendOneWay(Request request);

    NodeType nodeType();
}
