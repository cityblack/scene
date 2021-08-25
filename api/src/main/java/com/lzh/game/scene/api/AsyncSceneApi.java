package com.lzh.game.scene.api;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.lzh.game.scene.common.ContextConstant.ALL_MAP_LISTEN_KEY;

/**
 *
 *
 */
public interface AsyncSceneApi {

    /**
     * 主动请求创建场景, 将自动从场景管理中根据负载均衡算法自动找到对应的节点, 然后通知创建场景
     * @param group -- 游戏组
     * @param map -- 游戏地图
     * @param weight -- 权重, 该标识主要分辨场景是否是热点场景(人多)，尽量会避免热点场景在一起
     */
    void createScene(String group, int map, int weight);

    /**
     * 向场景管理中心订阅场景改变事件
     * @param group
     * @param status 需要注册的事件类型
     */
    default void subscribe(String group, SceneChangeStatus status, Consumer<SceneInstance> instance) {
        this.subscribe(group, status, ALL_MAP_LISTEN_KEY, instance);
    }

    /**
     * 向场景管理中心订阅场景改变事件 指定map
     * 相比{@link #subscribe(String, SceneChangeStatus, Consumer)} 力度小
     * @param group
     * @param status
     * @param map
     * @return
     */
    void subscribe(String group, SceneChangeStatus status, int map, Consumer<SceneInstance> instance);

    /**
     * 获取指定地图的所有实例
     * @param group
     * @param map
     */
    CompletableFuture<List<SceneInstance>> getSceneInstances(String group, int map);

    /**
     * 获取所有场景信息
     */
    default CompletableFuture<List<SceneInstance>> getAllSceneInstances(String group) {
        return this.getSceneInstances(group, ALL_MAP_LISTEN_KEY);
    }

    /**
     * 注册场景实例
     * @param group
     * @param instance
     */
    void registerSceneInstance(String group, SceneInstance instance);

    /**
     * 移除场景实例
     * @param group
     * @param instance
     */
    void removeSceneInstance(String group, SceneInstance instance);
}
