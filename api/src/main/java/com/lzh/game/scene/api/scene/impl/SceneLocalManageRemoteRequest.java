package com.lzh.game.scene.api.scene.impl;

import com.lzh.game.scene.api.AsyncSceneApi;
import com.lzh.game.scene.api.scene.SceneLocalManage;
import com.lzh.game.scene.common.SceneInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 直接请求调度服 不缓存
 */
public class SceneLocalManageRemoteRequest implements SceneLocalManage {

    private static final Logger logger = LoggerFactory.getLogger(SceneLocalManageRemoteRequest.class);

    private AsyncSceneApi api;

    public void setApi(AsyncSceneApi api) {
        this.api = api;
    }

    @Override
    public SceneInstance getSceneInstanceByKey(String group, String unique) {
        CompletableFuture<SceneInstance> future = api.getSceneInstance(group, unique);
        try {
            SceneInstance instance = future.get(5000, TimeUnit.SECONDS);
            return instance;
        } catch (Exception e) {
            logger.error("请求场景具体数据异常:", e);
        }
        return null;
    }

    @Override
    public SceneInstance addInstance(SceneInstance instance) {

        return instance;
    }
}
