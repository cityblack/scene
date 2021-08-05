package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.service.Replicator;
import com.lzh.game.scene.core.service.SceneService;

import java.util.List;

public class CpSceneServiceImpl implements SceneService {

    private SceneInstanceManage manage;

    private Replicator replicator;

    @Override
    public void registerSceneInstance(String group, SceneInstance instance) {

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
