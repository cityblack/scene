package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.ConnectManage;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;


/**
 * 连接管理器，所有的链接，心跳，亚健康处理都由其管理
 */
public interface ConnectClient {

    ApiConfig config();

    void start();

    /**
     * 只创建不持有
     * @param address
     * @param type
     * @return
     */
    SceneConnect createConnect(String address, NodeType type);

    SceneConnect createConnect(String host, int port, NodeType type);

    SceneConnectManage sceneConnectManage();

    ConnectManage connectManage();

    /**
     * 不存在就创建新的, 并持有
     * @param address
     * @param type
     * @return
     */
    SceneConnect getConnect(String address, NodeType type);

    SceneConnect getConnect(String host, int port, NodeType type);
}
