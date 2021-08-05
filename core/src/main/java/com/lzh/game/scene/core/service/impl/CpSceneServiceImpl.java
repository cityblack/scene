package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.service.Replicator;
import com.lzh.game.scene.core.service.SceneService;

import java.util.List;

public class CpSceneServiceImpl implements SceneService {

    private SceneInstanceManage manage;

    private Replicator replicator;

    @Override
    public void registerSceneInstance(SceneConnect connect, String group, SceneInstance instance) {

    }

    @Override
    public void removeSceneInstance(SceneConnect connect, String group, SceneInstance instance) {

    }

    @Override
    public void getAllSceneInstances(SceneConnect connect, String group) {

    }

    @Override
    public void getSceneInstances(SceneConnect connect, String group, int map) {

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
