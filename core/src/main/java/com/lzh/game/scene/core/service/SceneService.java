package com.lzh.game.scene.core.service;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.scene.SceneConnect;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.lzh.game.scene.common.ContextConstant.ALL_MAP_LISTEN_KEY;

public interface SceneService {

    /**
     * 注册场景实例
     * @param group
     * @param instance
     */
    void registerSceneInstance(String group, SceneInstance instance);

    /**
     * 移除场景实例
     * @param group
     * @param unique
     */
    void removeSceneInstance(String group, String unique);

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
    default void subscribe(Connect connect, String group, SceneChangeStatus status) {
        this.subscribe(connect, group, status, ALL_MAP_LISTEN_KEY);
    }

    /**
     * 向场景管理中心订阅场景改变事件 指定map
     * @param group
     * @param status
     * @param map
     * @return
     */
    void subscribe(Connect connect, String group, SceneChangeStatus status, int map);

    /**
     * 向中心注册保持地图实例数量, 当地图实例不足的时候(宕机, 失联)将自动向场景节点请求创建地图
     * @param map
     * @param numLimit
     */
    void keepMapInstances(String group, int map, int numLimit);

    /**
     *
     * @param group
     * @param mapId
     * @param weight
     */
    CompletableFuture<SceneInstance> createScene(SceneConnect connect, String group, int mapId, int weight);
}
