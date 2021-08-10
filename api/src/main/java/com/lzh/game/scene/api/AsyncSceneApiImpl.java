package com.lzh.game.scene.api;

import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.api.connect.sofa.SofaConnectClient;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Request;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AsyncSceneApiImpl implements AsyncSceneApi {

    private ConnectClient client;

    public AsyncSceneApiImpl(ConnectClient client) {
        this.client = client;
    }

    @Override
    public void createScene(String group, int map, int weight) {

    }

    @Override
    public void subscribe(String group, SceneChangeStatus status, Consumer<SceneInstance> instance) {

    }

    @Override
    public void subscribe(String group, SceneChangeStatus status, int map, Consumer<SceneInstance> instance) {

    }

    @Override
    public CompletableFuture<List<SceneInstance>> getSceneInstances(String group, int map) {
        return null;
    }

    @Override
    public CompletableFuture<List<SceneInstance>> getAllSceneInstances(String group) {
        return null;
    }

    @Override
    public void registerSceneInstance(String group, SceneInstance instance) {
        Request request = new Request();
        request.setId(1001);
        request.setParam(instance);
        client.sendOneWay(request);
    }

    @Override
    public CompletableFuture<Boolean> removeSceneInstance(String group, SceneInstance instance) {
        return null;
    }
}
