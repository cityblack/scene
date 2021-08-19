package com.lzh.game.scene.common.connect;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个Connect可能对应多个SceneConnect
 * 上锁考虑
 */
public class DefaultConnectManage implements SceneConnectManage, ConnectManage {

    // key -> SceneConnect#key 包装的connect
    private Map<String, SceneConnect> sceneConnects = new ConcurrentHashMap<>();

    private Map<NodeType, List<SceneConnect>> nodeConnect = new ConcurrentHashMap<>();
    // key -> Connect#key 通信的connect
    private Map<String, Connect> connects = new ConcurrentHashMap<>();

    private final static String SCENE_CONNECT = "scene_connect_relation";

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
        Connect connect = this.connects.remove(key);
        if (Objects.nonNull(connect)) {
            Set<String> sceneKeys = connect.getAttr(SCENE_CONNECT);
            if (Objects.isNull(sceneKeys) || sceneKeys.isEmpty()) {
                return;
            }
            for (String scene : sceneKeys) {
                removeSceneConnect(scene);
            }
        }
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
        this.addRelation(fact, key);
        this.putConnect(fact.key(), fact);
        NodeType type = connect.type();
        if (!this.nodeConnect.containsKey(type)) {
            this.nodeConnect.put(type, new ArrayList<>());
        }
        List<SceneConnect> list = this.nodeConnect.get(type);
        list.add(connect);
    }

    private void addRelation(Connect connect, String key) {
        Set<String> relations = connect.getAttr(SCENE_CONNECT);
        if (Objects.isNull(relations)) {
            relations = new HashSet<>();
            connect.setAttr(SCENE_CONNECT, relations);
        }
        relations.add(key);
    }

    @Override
    public ConnectManage connectManage() {
        return this;
    }

    @Override
    public void removeSceneConnect(String key) {
        SceneConnect connect = this.sceneConnects.remove(key);
        if (Objects.nonNull(connect)) {
            NodeType type = connect.type();
            List<SceneConnect> list = nodeConnect.get(type);
            if (Objects.nonNull(list) && !list.isEmpty()) {
                list.remove(connect);
            }
        }
    }
}
