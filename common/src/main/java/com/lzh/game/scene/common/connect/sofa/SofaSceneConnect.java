package com.lzh.game.scene.common.connect.sofa;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;

import java.util.concurrent.CompletableFuture;

/**
 * 包装connect对象
 */
public class SofaSceneConnect implements SceneConnect {

    private Connect connect;

    private NodeType type;

    private String key;

    public SofaSceneConnect(Connect connect, NodeType type, String key) {
        this.connect = connect;
        this.type = type;
        this.key = key;
    }

    @Override
    public void sendOneWay(Request request) {
        connect.sendOneWay(request);
    }

    @Override
    public CompletableFuture<Response> sendMessage(Request request) {
        return connect.sendMessage(request);
    }

    @Override
    public long reflectCount() {
        return connect.reflectCount();
    }

    @Override
    public <T> T getAttr(String key) {
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

    @Override
    public Connect bridge() {
        return connect;
    }
}
