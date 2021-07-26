package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.connect.sofa.SofaConnectClient;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SceneConnectClientImpl implements SceneConnectClient {

    private SofaConnectClient client;

    private Map<String, SceneConnect> connects = new ConcurrentHashMap<>();

    private Map<String, Set<NodeType>> connectNode = new ConcurrentHashMap<>();

    private Map<NodeType, List<SceneConnect>> nodeConnect = new ConcurrentHashMap<>();

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
        return null;
    }

    @Override
    public Collection<SceneConnect> getAllConnect() {
        return null;
    }

    @Override
    public boolean removeConnect(String key) {
        return false;
    }

    @Override
    public void shutdown() {
        client.shutdown();
    }

    @Override
    public void putConnect(String key, SceneConnect connect) {
        client.putConnect(connect.key(), connect);

//        client.putConnect(key, connect);

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
        return null;
    }

    @Override
    public SceneConnect getSceneConnect(String key) {
        return this.connects.get(key);
    }

    @Override
    public Set<NodeType> getConnectNodeType(SceneConnect connect) {
        return null;
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
        String key = type.getName() + connect.key();
        SofaSceneConnect wrapper = new SofaSceneConnect(connect, type, key);
        this.putConnect(key, wrapper);
        connect.setAttr(SCENE_CONNECT_KEY, key);
        return wrapper;
    }

    private class SofaSceneConnect implements SceneConnect {

        private Connect connect;

        private NodeType type;

        private String key;

        public SofaSceneConnect(Connect connect, NodeType type, String key) {
            this.connect = connect;
            this.type = type;
            this.key = key;
        }

        @Override
        public Response sendMessage(Request request) {
            return connect.sendMessage(request);
        }

        @Override
        public long reflectCount() {
            return connect.reflectCount();
        }

        @Override
        public Object getAttr(String key) {
            return connect.getAttr(key);
        }

        @Override
        public void setAttr(String key, Object o) {
            connect.setAttr(key, o);
        }

        @Override
        public NodeType type() {
            return type;
        }

        @Override
        public String key() {
            return key;
        }
    }


}
