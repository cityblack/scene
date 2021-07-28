package com.lzh.game.scene.core.service;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.SceneInstanceManage;

import java.util.List;

public class SceneServiceImpl implements SceneService {

    private SceneInstanceManage manage;

    @Override
    public SceneInstance registerSceneInstance(String group, SceneInstance instance) {
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
}
