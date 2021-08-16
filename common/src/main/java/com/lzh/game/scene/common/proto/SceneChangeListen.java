package com.lzh.game.scene.common.proto;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

public class SceneChangeListen {

    private String group;

    private SceneChangeStatus status;

    private SceneInstance instance;

    public SceneChangeStatus getStatus() {
        return status;
    }

    public void setStatus(SceneChangeStatus status) {
        this.status = status;
    }

    public SceneInstance getInstance() {
        return instance;
    }

    public void setInstance(SceneInstance instance) {
        this.instance = instance;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
