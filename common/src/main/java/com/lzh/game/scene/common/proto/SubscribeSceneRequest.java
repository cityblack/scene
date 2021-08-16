package com.lzh.game.scene.common.proto;

import com.lzh.game.scene.common.SceneChangeStatus;

public class SubscribeSceneRequest {

    private String group;

    private SceneChangeStatus status;

    private int map;

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

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }
}
