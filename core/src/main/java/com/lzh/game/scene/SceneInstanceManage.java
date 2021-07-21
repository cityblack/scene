package com.lzh.game.scene;

import com.lzh.game.scene.common.SceneInstance;

import java.util.List;

/**
 * 地图场景管理器
 */
public interface SceneInstanceManage {

    List<SceneInstance> get(String group);

    List<SceneInstance> get(String group, int mapId);

    SceneInstance getInstance(String unique);

    boolean put(String group, SceneInstance instance);

}
