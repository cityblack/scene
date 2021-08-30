package com.lzh.game.scene.core.service.impl.mode;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

public interface InstanceSubscribe {

    void addListener(String group, String connectKey, int map, SceneChangeStatus status);

    void notifyListener(String group, SceneInstance instance, SceneChangeStatus status);

    void removeListener(String connectKey);
}
