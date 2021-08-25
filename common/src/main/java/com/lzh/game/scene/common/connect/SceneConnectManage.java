package com.lzh.game.scene.common.connect;

import com.lzh.game.scene.common.connect.scene.SceneConnect;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上锁考虑
 */
public class SceneConnectManage implements ConnectManage<SceneConnect> {

    // key -> Connect#key 通信的connect
    private Map<String, SceneConnect> connects = new ConcurrentHashMap<>();

    @Override
    public SceneConnect getConnect(String key) {
        return connects.get(key);
    }

    @Override
    public Collection<SceneConnect> getAllConnect() {
        return connects.values();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public SceneConnect putConnect(String key, SceneConnect connect) {
        return this.connects.put(key, connect);
    }

    @Override
    public SceneConnect removeConnect(String key) {
        return this.removeConnect(key);
    }

    @Override
    public boolean contain(String key) {
        return this.connects.containsKey(key);
    }

    /*@Override
    public Connect putConnect(String key, Connect connect) {
        return this.connects.put(key, connect);
    }

    @Override
    public Connect removeConnect(String key) {
        Connect connect = this.connects.remove(key);
        if (Objects.nonNull(connect)) {
            Set<String> sceneKeys = connect.getAttr(SCENE_CONNECT);
            if (Objects.isNull(sceneKeys) || sceneKeys.isEmpty()) {
                return connect;
            }
            for (String scene : sceneKeys) {
                removeSceneConnect(scene);
            }
        }
        return connect;
    }*/

    /*@Override
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
    }*/
}
