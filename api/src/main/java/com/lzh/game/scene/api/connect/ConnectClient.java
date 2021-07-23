package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.common.NodeType;

import java.util.Collection;

/**
 * 连接管理器，所有的链接，心跳，亚健康处理都由其管理
 */
public interface ConnectClient {

    void init(ApiConfig config);

    Connect getConnect(String key);

    Collection<Connect> getAllConnect();

    void putConnect(String key, Connect connect);

    boolean removeConnect(String key);

    void shutdown();

    Connect createConnect(String address, NodeType type);
}
