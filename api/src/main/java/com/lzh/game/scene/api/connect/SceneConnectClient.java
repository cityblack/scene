package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;

import java.util.Collection;

/**
 * Connect针对连接是唯一的
 * SceneConnect针对连接+NodeType唯一, 有可能存在一个Connect是属于多NodeType
 */
public interface SceneConnectClient extends ConnectClient<Connect>, SceneConnectManage<SceneConnect> {

    /**
     * NodeType + connect#key
     * @param key
     * @return
     */
    @Override
    SceneConnect getConnect(String key);

    @Override
    boolean removeConnect(String key);

    @Override
    void putConnect(String key, SceneConnect connect);

    @Override
    Collection<SceneConnect> getAllConnect();

    SceneConnect createConnect(String address, NodeType type);

    SceneConnect createConnect(String host, int port, NodeType type);

}
