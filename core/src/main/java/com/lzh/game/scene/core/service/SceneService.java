package com.lzh.game.scene.core.service;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

import java.util.List;

public interface SceneService {

    /**
     * 注册场景实例
     * @param group
     * @param instance
     */
    SceneInstance registerSceneInstance(String group, SceneInstance instance);

    /**
     * 移除场景实例
     * @param group
     * @param instance
     */
    Boolean removeSceneInstance(String group, SceneInstance instance);

    /**
     * 获取所有场景
     * @param group
     * @return
     */
    List<SceneInstance> getAllSceneInstances(String group);

    /**
     * 获取所有场景 -- 指定地图
     * @param group
     * @param map
     * @return
     */
    List<SceneInstance> getSceneInstances(String group, int map);

    /**
     * 向场景管理中心订阅场景改变事件
     * @param group
     * @param status 需要注册的事件类型
     */
    void subscribe(String group, SceneChangeStatus status);

    /**
     * 向场景管理中心订阅场景改变事件 指定map
     * @param group
     * @param status
     * @param map
     * @return
     */
    void subscribe(String group, SceneChangeStatus status, int map);

}
