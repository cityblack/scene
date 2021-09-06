package com.lzh.game.scene.api;

import java.io.Serializable;
import java.util.Map;

public class TransportSceneData<K extends Serializable> {

    private int strategy;

    private String sceneKey;

    private K originKey;

    private Map<String, Object> extParam;

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public String getSceneKey() {
        return sceneKey;
    }

    public void setSceneKey(String sceneKey) {
        this.sceneKey = sceneKey;
    }

    public K getOriginKey() {
        return originKey;
    }

    public void setOriginKey(K originKey) {
        this.originKey = originKey;
    }

    public Map<String, Object> getExtParam() {
        return extParam;
    }

    public void setExtParam(Map<String, Object> extParam) {
        this.extParam = extParam;
    }

    public TransportSceneData(int strategy, String sceneKey, K originKey) {
        this.strategy = strategy;
        this.sceneKey = sceneKey;
        this.originKey = originKey;
    }

    public TransportSceneData(int strategy, String sceneKey, K originKey, Map<String, Object> extParam) {
        this.strategy = strategy;
        this.sceneKey = sceneKey;
        this.originKey = originKey;
        this.extParam = extParam;
    }
}
