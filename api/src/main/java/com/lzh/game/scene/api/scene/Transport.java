package com.lzh.game.scene.api.scene;

import com.lzh.game.scene.api.TransportSceneData;

import java.io.Serializable;

public interface Transport {

    /**
     * 进入指定的场景
     * @param sceneKey
     */
    <K extends Serializable>void transportScene(String group, String sceneKey, TransportSceneData<K> request);

    /**
     * 进入指定地图 动态分配
     * @param group
     * @param map
     */
    <K extends Serializable>void transportScene(String group, int map, TransportSceneData<K> request);
}
