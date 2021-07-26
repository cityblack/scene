package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.common.connect.Connect;


/**
 * 连接管理器，所有的链接，心跳，亚健康处理都由其管理
 */
public interface ConnectClient<T extends Connect> {

    void init(ApiConfig config);

    T createConnect(String address);

    T createConnect(String host, int port);

    void onConnectClose(T connect);
}
