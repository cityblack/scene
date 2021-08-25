package com.lzh.game.scene.core.service.impl.mode;

import com.lzh.game.scene.common.SceneInstance;

public class SceneInstanceTop {

    private SceneInstance sceneInstance;

    private int eventType;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public SceneInstance getSceneInstance() {
        return sceneInstance;
    }

    public void setSceneInstance(SceneInstance sceneInstance) {
        this.sceneInstance = sceneInstance;
    }

    public static SceneInstanceTop of(SceneInstance sceneInstance, int eventType) {
        SceneInstanceTop top = new SceneInstanceTop();
        top.sceneInstance = sceneInstance;
        top.eventType = eventType;
        return top;
    }
}
