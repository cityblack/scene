package com.lzh.game.scene.api;

import com.lzh.game.scene.api.connect.ConnectClient;
import static com.lzh.game.scene.common.RequestSpace.*;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;

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
        Request request = Request.of(cmd(INSTANCE_SPACE, 2));
        CompletableFuture<Response<List<SceneInstance>>> future = client.sendMessage(request);
        return future
                .thenApply(Response::getParam);
    }

    @Override
    public CompletableFuture<List<SceneInstance>> getAllSceneInstances(String group) {
        Request request = Request.of(cmd(INSTANCE_SPACE, 3), group);
        CompletableFuture<Response<List<SceneInstance>>> future = client.sendMessage(request);
        return future.thenApply(Response::getParam);
    }

    @Override
    public void registerSceneInstance(String group, SceneInstance instance) {
        instance.setGroup(group);
        Request request = buildSceneRequest(instance, INSTANCE_SPACE, 1);
        client.sendOneWay(request);
    }

    @Override
    public void removeSceneInstance(String group, SceneInstance instance) {
    }

    protected Request buildSceneRequest(SceneInstance instance, int space, int target) {
        return Request.of(cmd(space, target), instance);
    }
}
