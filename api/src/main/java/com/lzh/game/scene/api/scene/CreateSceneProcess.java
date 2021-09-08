package com.lzh.game.scene.api.scene;

import com.lzh.game.scene.common.SceneInstance;

/**
 * 场景节点 接受创建创建请求
 */
@FunctionalInterface
public interface CreateSceneProcess {

    SceneInstance createScene(int map);
}
