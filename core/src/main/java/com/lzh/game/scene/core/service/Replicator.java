package com.lzh.game.scene.core.service;

import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.jrfa.rpc.entity.Response;

import java.util.concurrent.CompletableFuture;

/**
 * 内部请求接口 所有的内部请求都往这里添加
 */
public interface Replicator {

    CompletableFuture<Response> registerSceneInstance(SceneInstance sceneInstance);
}
