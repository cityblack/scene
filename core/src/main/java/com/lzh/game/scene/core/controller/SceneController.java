package com.lzh.game.scene.core.controller;

import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.cmd.Action;
import com.lzh.game.scene.common.connect.server.cmd.Cmd;
import com.lzh.game.scene.common.proto.CreateSceneRequest;
import com.lzh.game.scene.common.proto.MapSceneRequest;
import com.lzh.game.scene.common.proto.SubscribeSceneRequest;
import com.lzh.game.scene.core.service.SceneService;

import java.util.List;

import static com.lzh.game.scene.common.RequestSpace.*;

@Action(INSTANCE_SPACE)
public class SceneController {

    private SceneService sceneService;

    public void setSceneService(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @Cmd(INSTANCE_REGISTER)
    public void register(SceneConnect connect, SceneInstance instance) {
        instance.setAddress(connect.key());
        sceneService.registerSceneInstance(instance.getGroup(), instance);
    }

    @Cmd(INSTANCE_GET)
    public List<SceneInstance> getScenes(MapSceneRequest request) {
        return sceneService.getSceneInstances(request.getGroup(), request.getMap());
    }

    @Cmd(INSTANCE_SUBSCRIBE)
    public void subscribe(SceneConnect connect, SubscribeSceneRequest request) {
        sceneService.subscribe(connect, request.getGroup(), request.getStatus());
    }

    @Cmd(SCENE_CREATE)
    public void createScene(SceneConnect connect, CreateSceneRequest request) {
        sceneService.createScene(connect, request.getGroup(), request.getMap(), request.getWeight());
    }
}
