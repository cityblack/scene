package com.lzh.game.scene.api.scene;

import com.lzh.game.scene.common.SceneInstance;

/**
 * 缓存或者不缓存场景实例 根据配置加载不同实现
 */
public interface SceneLocalManage {

    SceneInstance getSceneInstanceByKey(String group, String unique);

    SceneInstance addInstance(SceneInstance instance);
}
