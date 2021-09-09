package com.lzh.game.scene.api.scene;

import com.lzh.game.scene.api.AsyncSceneApi;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.api.scene.transport.AbstractTransport;
import com.lzh.game.scene.api.scene.transport.AbstractTransportRemote;
import com.lzh.game.scene.common.connect.scene.SceneConnect;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 场景节点Bootstrap
 */
public class SceneNodeBootstrap {

    private CreateSceneProcess process;

    private ConnectClient<SceneConnect> client;

    private AsyncSceneApi api;

    private Map<Integer, AbstractTransport<?>> transport = new HashMap<>();

    private Map<Integer, AbstractTransportRemote<?, ?>> transportRemote = new HashMap<>();

    public void start() {
        if (Objects.isNull(process)) {
            throw new IllegalArgumentException("Must defined CreateSceneProcess");
        }
        if (transport.isEmpty()) {
            throw new IllegalArgumentException("Transport implements is null");
        }
        if (transportRemote.isEmpty()) {
            throw new IllegalArgumentException("TransportRemote implements is null");
        }
    }

    public void registerTransport(int strategy, AbstractTransport<?> transport) {
        this.transport.put(strategy, transport);
    }

    public AbstractTransport<?> getTransport(int strategy) {
        return this.transport.get(strategy);
    }

    public void registerTransportRemote(int strategy, AbstractTransportRemote<?, ?> transport) {
        this.transportRemote.put(strategy, transport);
    }

    public AbstractTransportRemote<?, ?> getTransportRemote(int strategy) {
        return this.transportRemote.get(strategy);
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
