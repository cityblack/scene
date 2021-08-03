package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.SceneInstanceManage;
import com.lzh.game.scene.core.exchange.DataRequest;
import com.lzh.game.scene.core.jrfa.JRService;
import com.lzh.game.scene.core.service.SceneService;

import java.util.List;

public class CpSceneServiceImpl implements SceneService {

    private SceneInstanceManage manage;

    private JRService jrService;

    @Override
    public SceneInstance registerSceneInstance(String group, SceneInstance instance) {
        DataRequest request = new DataRequest();
        jrService.commitTask(request);
        return null;
    }

    @Override
    public Boolean removeSceneInstance(String group, SceneInstance instance) {
        return null;
    }

    @Override
    public List<SceneInstance> getAllSceneInstances(String group) {
        return null;
    }

    @Override
    public List<SceneInstance> getSceneInstances(String group, int map) {
        return manage.get(group, map);
    }

    @Override
    public void subscribe(String group, SceneChangeStatus status) {

    }

    @Override
    public void subscribe(String group, SceneChangeStatus status, int map) {

    }

    @Override
    public void keepMapInstances(String group, int map, int numLimit) {

    }
}
