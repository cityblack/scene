package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.service.SceneService;
import org.redisson.api.RedissonClient;

public class RedisSceneServiceImpl implements SceneService {

    private RedissonClient client;

    private SceneInstanceManage manage;

    @Override
    public void registerSceneInstance(Response response, String group, SceneInstance instance) {

    }

    @Override
    public void removeSceneInstance(Response response, String group, SceneInstance instance) {

    }

    @Override
    public void getAllSceneInstances(Response response, String group) {

    }

    @Override
    public void getSceneInstances(Response response, String group, int map) {

    }

    @Override
    public void subscribe(SceneConnect connect, String group, SceneChangeStatus status, int map) {

    }

    @Override
    public void keepMapInstances(Response response, String group, int map, int numLimit) {

    }
}
