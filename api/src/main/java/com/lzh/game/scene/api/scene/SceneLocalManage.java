package com.lzh.game.scene.api.scene;

import com.lzh.game.scene.common.SceneInstance;

public interface SceneLocalManage {

    SceneInstance getSceneInstanceByKey(String group, String unique);
}
