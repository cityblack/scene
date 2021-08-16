package com.lzh.game.scene.common.event;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

public class SceneInstanceEvent {

    private String group;

    private SceneChangeStatus status;

    private SceneInstance instance;

    public SceneInstanceEvent(String group, SceneChangeStatus status, SceneInstance instance) {
        this.group = group;
        this.status = status;
        this.instance = instance;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

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
}
