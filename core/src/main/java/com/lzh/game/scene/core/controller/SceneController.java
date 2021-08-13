package com.lzh.game.scene.core.controller;

import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.server.cmd.Action;
import com.lzh.game.scene.common.connect.server.cmd.Cmd;
import com.lzh.game.scene.core.service.SceneService;

import static com.lzh.game.scene.common.RequestSpace.INSTANCE_SPACE;

@Action(INSTANCE_SPACE)
public class SceneController {

    private SceneService sceneService;

    public void setSceneService(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @Cmd(1)
    public void register(Response response, SceneInstance instance) {
        sceneService.registerSceneInstance(response, instance.getGroup(), instance);
    }

    @Cmd(2)
    public void getScenes(Response response, String group) {
        sceneService.getAllSceneInstances(response, group);
    }
}
