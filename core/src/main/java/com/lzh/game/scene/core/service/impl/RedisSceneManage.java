package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import org.redisson.api.RedissonClient;

import java.util.List;

public class RedisSceneManage implements SceneInstanceManage {

    private RedissonClient client;

    public RedisSceneManage(RedissonClient client) {
        this.client = client;
    }

    @Override
    public List<SceneInstance> get(String group) {
        
        return null;
    }

    @Override
    public List<SceneInstance> get(String group, int mapId) {
        return null;
    }

    @Override
    public SceneInstance getInstance(String group, String unique) {
        return null;
    }

    @Override
    public boolean put(String group, SceneInstance instance) {
        return false;
    }
}
