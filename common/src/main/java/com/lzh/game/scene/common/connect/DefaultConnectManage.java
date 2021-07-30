package com.lzh.game.scene.common.connect;

import com.lzh.game.scene.common.ContextDefined;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class DefaultConnectManage implements SceneConnectManage, ConnectManage {

    // key -> SceneConnect#key 包装的connect
    private Map<String, SceneConnect> sceneConnects = new ConcurrentHashMap<>();

    private Map<NodeType, List<SceneConnect>> nodeConnect = new ConcurrentHashMap<>();
    // key -> Connect#key 通信的connect
    private Map<String, Connect> connects = new ConcurrentHashMap<>();

    @Override
    public Connect getConnect(String key) {
        return connects.get(key);
    }

    @Override
    public Collection<Connect> getAllConnect() {
        return connects.values();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public Connect putConnect(String key, Connect connect) {
        return this.connects.put(key, connect);
    }

    @Override
    public void removeConnect(String key) {
        this.connects.remove(key);
    }

    @Override
    public List<SceneConnect> getByNode(NodeType type) {
        return new ArrayList<>(this.nodeConnect.get(type));
    }

    @Override
    public SceneConnect getSceneConnect(String key) {
        return this.sceneConnects.get(key);
    }

    @Override
    public void putSceneConnect(String key, SceneConnect connect) {
        this.sceneConnects.put(key, connect);
        Connect fact = connect.bridge();
        this.putConnect(fact.key(), fact);
        NodeType type = connect.type();
        if (!this.nodeConnect.containsKey(type)) {
            this.nodeConnect.put(type, new ArrayList<>());
        }
        List<SceneConnect> list = this.nodeConnect.get(type);
        list.add(connect);
    }

    @Override
    public ConnectManage connectManage() {
        return this;
    }

    @Override
    public void removeSceneConnect(String key) {
        SceneConnect connect = this.sceneConnects.remove(key);
        if (Objects.isNull(connect)) {
            return;
        }
        String connectKey = connect.getAttr(ContextDefined.SCENE_CONNECT_KEY);
        removeConnect(connectKey);
    }
}
