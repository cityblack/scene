package com.lzh.game.scene.api.scene;

import com.lzh.game.scene.api.AsyncSceneApi;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.common.connect.scene.SceneConnect;

import java.util.Objects;

/**
 * 场景节点Bootstrap
 */
public class SceneNodeBootstrap {

    private CreateSceneProcess process;

    private ConnectClient<SceneConnect> client;

    private AsyncSceneApi api;

    public void start() {
        if (Objects.isNull(process)) {
            throw new IllegalArgumentException("Must defined CreateSceneProcess");
        }
    }

    // === GET SET ===
    public CreateSceneProcess getProcess() {
        return process;
    }

    public void setProcess(CreateSceneProcess process) {
        this.process = process;
    }

    public ConnectClient<SceneConnect> getClient() {
        return client;
    }

    public void setClient(ConnectClient<SceneConnect> client) {
        this.client = client;
    }

    public AsyncSceneApi getApi() {
        return api;
    }

    public void setApi(AsyncSceneApi api) {
        this.api = api;
    }
}
