package com.lzh.game.scene.common.connect.scene;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.ConnectManage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SceneConnectManage {

    List<SceneConnect> getByNode(NodeType type);

    /**
     * NodeType + Connect#Key 区别getConnect
     * @param key
     * @return
     */
    SceneConnect getSceneConnect(String key);

    void putSceneConnect(String key, SceneConnect connect);

    ConnectManage connectManage();

    void removeSceneConnect(String key);
}
