package com.lzh.game.scene.core.service;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;

import java.util.List;

public interface SceneService {

    /**
     * 注册场景实例
     * @param group
     * @param instance
     */
    void registerSceneInstance(Response response, String group, SceneInstance instance);

    /**
     * 移除场景实例
     * @param group
     * @param instance
     */
    void removeSceneInstance(Response response, String group, SceneInstance instance);

    /**
     * 获取所有场景
     * @param group
     * @return
     */
    void getAllSceneInstances(Response response, String group);

    /**
     * 获取所有场景 -- 指定地图
     * @param group
     * @param map
     * @return
     */
    void getSceneInstances(Response response, String group, int map);

    /**
     * 向场景管理中心订阅场景改变事件
     * @param group
     * @param status 需要注册的事件类型
     */
    void subscribe(Response response, String group, SceneChangeStatus status);

    /**
     * 向场景管理中心订阅场景改变事件 指定map
     * @param group
     * @param status
     * @param map
     * @return
     */
    void subscribe(Response response, String group, SceneChangeStatus status, int map);

    /**
     * 向中心注册保持地图实例数量, 当地图实例不足的时候(宕机, 失联)将自动向场景节点请求创建地图
     * @param map
     * @param numLimit
     */
    void keepMapInstances(Response response, String group, int map, int numLimit);
}
