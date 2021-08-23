package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.service.SceneService;
import org.redisson.api.RedissonClient;

import java.util.List;

public class RedisSceneServiceImpl implements SceneService {

    private RedissonClient client;

    private SceneInstanceManage manage;

    @Override
    public void registerSceneInstance(String group, SceneInstance instance) {

    }

    @Override
    public void removeSceneInstance(String group, SceneInstance instance) {

    }

    @Override
    public List<SceneInstance> getSceneInstances(String group, int map) {
        return null;
    }

    @Override
    public void subscribe(Connect connect, String group, SceneChangeStatus status, int map) {

    }

    @Override
    public void keepMapInstances(String group, int map, int numLimit) {

    }
}
