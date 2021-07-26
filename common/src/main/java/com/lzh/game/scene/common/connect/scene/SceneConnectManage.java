package com.lzh.game.scene.common.connect.scene;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.ConnectManage;

import java.util.Collection;
import java.util.Set;

public interface SceneConnectManage<T extends SceneConnect>
        extends ConnectManage<T> {

    Collection<T> getByNode(NodeType type);

    /**
     * NodeType + Connect#Key 区别getConnect
     * @param key
     * @return
     */
    T getSceneConnect(String key);

    /**
     * 一个连接可能有多个类型
     * @param connect
     * @return
     */
    Set<NodeType> getConnectNodeType(T connect);

}
