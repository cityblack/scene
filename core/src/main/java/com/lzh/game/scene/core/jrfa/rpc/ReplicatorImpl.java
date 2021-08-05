package com.lzh.game.scene.core.jrfa.rpc;

import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.jrfa.JRService;
import com.lzh.game.scene.core.service.Replicator;

import java.util.concurrent.CompletableFuture;

public class ReplicatorImpl implements Replicator {

    private JRService jrService;

    @Override
    public CompletableFuture<Boolean> registerSceneInstance(SceneInstance sceneInstance) {

        return null;
    }
}
