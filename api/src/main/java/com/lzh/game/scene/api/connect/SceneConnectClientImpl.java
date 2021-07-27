package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.connect.sofa.SofaConnectClient;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.sofa.SofaSceneConnect;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Wrapper Sofa connect client
 */
public class SceneConnectClientImpl implements SceneConnectClient {

    private SofaConnectClient client;

    private Map<String, SceneConnect> connects = new ConcurrentHashMap<>();

    // connect key -> Set<NodeType>
    private Map<String, Set<NodeType>> connectNode = new ConcurrentHashMap<>();

    private Map<NodeType, List<SceneConnect>> nodeConnect = new ConcurrentHashMap<>();

    public SceneConnectClientImpl(SofaConnectClient client) {
        this.client = client;
    }

    @Override
    public void init(ApiConfig config) {
        client.init(config);
    }

    @Override
    public Connect createConnect(String address) {

        return client.createConnect(address);
    }

    @Override
    public Connect createConnect(String host, int port) {
        return client.createConnect(host, port);
    }

    @Override
    public SceneConnect getConnect(String key) {
        return this.connects.get(key);
    }

    @Override
    public Collection<SceneConnect> getAllConnect() {
        return this.connects.values();
    }

    @Override
    public boolean removeConnect(String key) {
        SceneConnect connect = this.connects.remove(key);
        if (Objects.isNull(connect)) {
            return false;
        }
        NodeType type = connect.type();
        this.nodeConnect.getOrDefault(type, new ArrayList<>()).remove(connect);
        this.connectNode.remove(key);

        String address = connect.address();
        client.removeConnect(address);

        return true;
    }

    @Override
    public void shutdown() {
        client.shutdown();
    }

    @Override
    public void putConnect(String key, SceneConnect connect) {
        client.putConnect(connect.address(), connect);
        NodeType type = connect.type();
        this.nodeConnect.merge(type, Arrays.asList(connect), (o1, o2) -> {
            o1.addAll(o2);
            return o1;
        });
        String unique = connect.address();
        this.connects.put(unique, connect);
        Set<NodeType> set = new HashSet<>(1);
        set.add(type);
        this.connectNode.merge(key, set , (o1, o2) -> {
            o1.addAll(o2);
            return o1;
        });
    }

    @Override
    public SceneConnect createConnect(String address, NodeType type) {
        Connect connect = getOrCreateConnect(address);
        return wrapper(connect, type);
    }

    @Override
    public SceneConnect createConnect(String host, int port, NodeType type) {
        Connect connect = getOrCreateConnect(host, port);
        return wrapper(connect, type);
    }

    @Override
    public void onConnectClose(Connect connect) {

    }

    @Override
    public Collection<SceneConnect> getByNode(NodeType type) {
        return this.nodeConnect.getOrDefault(type, Collections.EMPTY_LIST);
    }

    @Override
    public SceneConnect getSceneConnect(String key) {
        return this.connects.get(key);
    }

    @Override
    public Set<NodeType> getConnectNodeType(SceneConnect connect) {
        return this.connectNode.getOrDefault(connect, Collections.EMPTY_SET);
    }

    private Connect getOrCreateConnect(String address) {
        Connect connect = this.client.getConnect(address);
        if (Objects.nonNull(connect)) {
            return connect;
        }
        return client.createConnect(address);
    }

    private Connect getOrCreateConnect(String host, int port) {
        Connect connect = client.getConnect(toAddress(host, port));
        if (Objects.nonNull(connect)) {
            return connect;
        }
        return client.createConnect(host, port);
    }

    private SceneConnect wrapper(Connect connect, NodeType type) {
        String key = type.getName() + connect.address();
        SofaSceneConnect wrapper = new SofaSceneConnect(connect, type, key);
        this.putConnect(key, wrapper);
        connect.setAttr(SCENE_CONNECT_KEY, key);
        return wrapper;
    }
}
