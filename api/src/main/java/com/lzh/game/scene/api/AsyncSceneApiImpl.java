package com.lzh.game.scene.api;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncSceneApiImpl implements AsyncSceneApi {

    @Override
    public CompletableFuture<SceneInstance> createScene(String group, int map, int weight) {
        return null;
    }

    @Override
    public CompletableFuture<SceneInstance> subscribe(String group, SceneChangeStatus status) {
        return null;
    }

    @Override
    public CompletableFuture<SceneInstance> subscribe(String group, SceneChangeStatus status, int map) {
        return null;
    }

    @Override
    public CompletableFuture<List<SceneInstance>> getSceneInstances(String group, int map) {
        return null;
    }

    @Override
    public CompletableFuture<List<SceneInstance>> getAllSceneInstances(String group) {
        return null;
    }

    @Override
    public CompletableFuture<SceneInstance> registerSceneInstance(String group, SceneInstance instance) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> removeSceneInstance(String group, SceneInstance instance) {
        return null;
    }
}
