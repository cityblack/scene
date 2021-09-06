package com.lzh.game.scene.api.scene;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

import java.util.function.Consumer;

public interface SceneService {

    /**
     * @param group
     * @param instance
     * @param status
     */
    void onSceneChange(String group, SceneInstance instance, SceneChangeStatus status);

    void addSceneChangeListen(String group, int mapId, Consumer<SceneInstance> consumer);
}
