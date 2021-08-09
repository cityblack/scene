package com.lzh.game.scene.core.jrfa;

import com.lzh.game.scene.common.SceneInstance;

import java.util.concurrent.CompletableFuture;

/**
 * 内部请求接口 所有的内部请求都往这里添加
 */
public interface Replicator {

    CompletableFuture<Void> registerSceneInstance(SceneInstance sceneInstance);
}
