package com.lzh.game.scene.api;

import com.lzh.game.scene.api.connect.ConnectClient;

import static com.lzh.game.scene.api.scene.TransportLocal.NULL_INSTANCE;
import static com.lzh.game.scene.common.RequestSpace.*;

import com.lzh.game.scene.api.scene.*;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.proto.CreateSceneRequest;
import com.lzh.game.scene.common.proto.GetUniqueSceneInstanceRequest;
import com.lzh.game.scene.common.proto.MapSceneRequest;
import com.lzh.game.scene.common.proto.SubscribeSceneRequest;
import com.lzh.game.scene.common.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AsyncSceneApiImpl implements AsyncSceneApi {

    private static final Logger logger = LoggerFactory.getLogger(AsyncSceneApiImpl.class);

    private ConnectClient<SceneConnect> client;

    private SceneService sceneService;

    public AsyncSceneApiImpl(ConnectClient<SceneConnect> client, SceneService sceneService) {
        this.client = client;
        this.sceneService = sceneService;
    }

    @Override
    public void createScene(String group, int map, int weight, Consumer<SceneInstance> instance) {
        CreateSceneRequest request = new CreateSceneRequest();
        request.setGroup(group);
        request.setWeight(weight);
        request.setMap(map);

        Request value = Request.of(cmd(INSTANCE_SPACE, SCENE_CREATE), request);
        if (Objects.isNull(instance)) {
            client.sendOneWay(value);
        } else {
            CompletableFuture<Response<SceneInstance>> response = client.sendMessage(value);
            response
                    .thenApply(Response::getParam)
                    .thenAccept(e -> instance.accept(e));
        }
    }

    @Override
    public void subscribe(String group, SceneChangeStatus status, int map, Consumer<SceneInstance> instance) {
        SubscribeSceneRequest sceneRequest = new SubscribeSceneRequest();
        sceneRequest.setGroup(group);
        sceneRequest.setStatus(status);
        sceneRequest.setMap(map);

        Request request = buildRequest(INSTANCE_SUBSCRIBE, sceneRequest);

        client.sendMessage(request).thenAccept(response -> {
            logger.info("Add subscribe!");
            sceneService.addSceneChangeListen(group, map, instance);
        });
    }

    @Override
    public CompletableFuture<List<SceneInstance>> getSceneInstances(String group, int map) {
        MapSceneRequest sceneRequest = new MapSceneRequest();
        sceneRequest.setGroup(group);
        sceneRequest.setMap(map);
        Request request = buildRequest(INSTANCE_GET, sceneRequest);

        CompletableFuture<Response<List<SceneInstance>>> future = client.sendMessage(request);
        return future
                .thenApply(Response::getParam);
    }

    @Override
    public CompletableFuture<SceneInstance> getSceneInstance(String group, String unique) {
        GetUniqueSceneInstanceRequest data = new GetUniqueSceneInstanceRequest();
        data.setGroup(group);
        data.setUnique(unique);

        Request request = buildRequest(INSTANCE_UNIQUE_GET, data);
        CompletableFuture<Response<SceneInstance>> future = client.sendMessage(request);
        return future.thenApply(Response::getParam);
    }

    @Override
    public void registerSceneInstance(String group, SceneInstance instance) {
        instance.setGroup(group);
        Request request = buildRequest(INSTANCE_REGISTER, instance);
        client.sendOneWay(request);
    }

    @Override
    public void removeSceneInstance(String group, SceneInstance instance) {
    }

    protected Request buildRequest(int target, Object o) {
        return Request.of(cmd(INSTANCE_SPACE, target), o);
    }
}
