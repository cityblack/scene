package com.lzh.game.scene.common;

public interface SceneInstance {

    String group();
    /**
     * 地图实例唯一标识
     * @return
     */
    String unique();

    /**
     * 对应的mapId
     * @return
     */
    int map();


}
