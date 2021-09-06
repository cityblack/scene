package com.lzh.game.scene.core.service.impl;

import com.google.common.eventbus.Subscribe;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.ConnectEvent;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.utils.EventBusUtils;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.service.SceneService;
import com.lzh.game.scene.core.service.impl.mode.InstanceSubscribe;
import com.lzh.game.scene.core.service.impl.mode.InstanceSubscribeListener;
import com.lzh.game.scene.core.service.impl.mode.SceneInstanceTop;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 使用redis做共享 满足ap
 */
public class RedisSceneServiceImpl implements SceneService {

    private static final Logger logger = LoggerFactory.getLogger(RedisSceneServiceImpl.class);

    private static final String INSTANCE_TOP = "map_instance_top";

    private static final String INSTANCE_PRE = "instance_";

    private static final Function<String, String> TO_INSTANCE_PRE = s -> INSTANCE_PRE + s;

    private RedissonClient client;

    private InstanceSubscribe listener;

    public RedisSceneServiceImpl(RedissonClient client, InstanceSubscribe listener) {
        this.client = client;
        this.listener = listener;
        this.init();
    }

    protected void init() {
        this.client.getTopic(INSTANCE_TOP)
                .addListenerAsync(SceneInstanceTop.class, this::onMessage)
                .onComplete((id, throwable) -> {
                    if (Objects.nonNull(throwable)) {
                        logger.error("Register instance listener error!", throwable);
                    } else {
                        logger.info("Register redis top. id:{}", id);
                    }
                });
        EventBusUtils.getInstance().register(this);
    }

    /**
     * 批量注册 批量推送?? 延迟队列?
     *
     * @param group
     * @param instance
     */
    @Override
    public void registerSceneInstance(String group, SceneInstance instance) {

        RMap<String, SceneInstance> contain = getGroupContain(group);
        String key = instance.getUnique();
        if (contain.containsKey(key)) {
            logger.error("Repeated registration instance [{}-{}-{}]", instance.getGroup(), instance.getMap(), instance.getUnique());
        }
        contain.put(key, instance);
        logger.info("Register instance:{}", instance);
        RSet<String> keys = getInstanceMapKeys(group, instance.getMap());
        keys.add(key);

        SceneInstanceTop top = SceneInstanceTop.of(instance, SceneChangeStatus.CHANGE.ordinal());
        this.client
                .getTopic(INSTANCE_TOP)
                .publish(top);
    }

    @Override
    public void removeSceneInstance(String group, String unique) {
        RMap<String, SceneInstance> contain = getGroupContain(group);
        SceneInstance instance = contain.remove(unique);
        if (Objects.nonNull(instance)) {
            SceneInstanceTop message = SceneInstanceTop.of(instance, SceneChangeStatus.DESTROY.ordinal());
            getInstanceMapKeys(group, instance.getMap()).remove(instance.getGroup());
            this.client
                    .getTopic(INSTANCE_TOP)
                    .publish(message);
        }
    }

    @Override
    public List<SceneInstance> getSceneInstances(String group, int map) {
        if (map == ContextConstant.ALL_MAP_LISTEN_KEY) {
            return new ArrayList<>(getGroupContain(group).values());
        }
        RMap<String, SceneInstance> contain = getGroupContain(group);
        RSet<String> keys = getInstanceMapKeys(group, map);
        return new ArrayList<>(contain.getAll(keys).values());
    }

    @Override
    public void subscribe(Connect connect, String group, SceneChangeStatus status, int map) {
        this.listener.addListener(group, connect.key(), map, status);
    }

    @Override
    public void keepMapInstances(String group, int map, int numLimit) {

    }

    @Override
    public void createScene(SceneConnect connect, String group, int mapId, int weight) {
        
    }

    /**
     * Redis获取订阅的场景信息 进行推送
     *
     * @param channel
     * @param msg
     */
    private void onMessage(CharSequence channel, SceneInstanceTop msg) {
        SceneInstance instance = msg.getSceneInstance();
        this.listener
                .notifyListener(instance.getGroup(), instance, SceneChangeStatus.values()[msg.getEventType()]);
    }

    public void setClient(RedissonClient client) {
        this.client = client;
    }

    public RMap<String, SceneInstance> getContain(String group) {
        return this.client.getMap(group);
    }

    protected RMap<String, SceneInstance> getGroupContain(String group) {
        return this.getContain(TO_INSTANCE_PRE.apply(group));
    }

    private String getInstanceMapIndexKey(String group, int map) {
        return TO_INSTANCE_PRE.apply(group) + "_" + map;
    }

    private RSet<String> getInstanceMapKeys(String group, int map) {
        return this.client.getSet(getInstanceMapIndexKey(group, map));
    }

    @Subscribe
    public void onConnectClose(ConnectEvent connectEvent) {
        if (connectEvent.getType() != ConnectEvent.CLOSED) {
            return;
        }
        SceneConnect connect = connectEvent.getConnect().getAttr(ContextConstant.SCENE_CONNECT_RELATION);
        if (Objects.isNull(connect)) {
            return;
        }
        this.listener
                .removeListener(connect.key());
    }
}
