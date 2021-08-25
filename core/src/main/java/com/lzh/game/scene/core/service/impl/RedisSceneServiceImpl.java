package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.service.SceneService;
import com.lzh.game.scene.core.service.impl.mode.SceneInstanceTop;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RedisSceneServiceImpl implements SceneService {

    private static final Logger logger = LoggerFactory.getLogger(RedisSceneServiceImpl.class);

    private RedissonClient client;

    private SceneInstanceManage manage;

    private static final String INSTANCE_TOP = "map_instance_top";

    public RedisSceneServiceImpl(RedissonClient client, SceneInstanceManage manage) {
        this.client = client;
        this.manage = manage;
        this.init();
    }

    protected void init() {
        this.client.getTopic(INSTANCE_TOP)
                .addListenerAsync(SceneInstanceTop.class, this::onMessage)
                .onComplete((id, throwable) -> {
                    if (Objects.nonNull(throwable)) {
                        logger.error("Register instance listener error!", throwable);
                    }
                });
    }

    @Override
    public void registerSceneInstance(String group, SceneInstance instance) {

        RMap<String, Object> contain = getContain(group);
        String key = instance.getUnique();
        if (contain.containsKey(key)) {
            logger.error("Repeated registration instance [{}-{}-{}]", instance.getGroup(), instance.getMap(), instance.getUnique());
        }
        contain.put(key, instance);
        logger.info("Register instance:{}", instance);
        SceneInstanceTop top = SceneInstanceTop.of(instance, SceneChangeStatus.CHANGE.ordinal());
        this.client
                .getTopic(INSTANCE_TOP)
                .publish(top);
    }

    @Override
    public void removeSceneInstance(String group, String unique) {
        RMap<String, Object> contain = getContain(group);
        Object value = contain.remove(unique);
        if (Objects.nonNull(value)) {
            SceneInstance instance = (SceneInstance) value;
            SceneInstanceTop message = SceneInstanceTop.of(instance, SceneChangeStatus.DESTROY.ordinal());
            this.client
                    .getTopic(INSTANCE_TOP)
                    .publish(message);
        }
    }

    @Override
    public List<SceneInstance> getSceneInstances(String group, int map) {
        return null;
    }

    @Override
    public void subscribe(Connect connect, String group, SceneChangeStatus status, int map) {

    }

    @Override
    public void keepMapInstances(String group, int map, int numLimit) {

    }

    /**
     * @param channel
     * @param msg
     */
    private void onMessage(CharSequence channel, SceneInstanceTop msg) {

    }

    public void setClient(RedissonClient client) {
        this.client = client;
    }

    public void setManage(SceneInstanceManage manage) {
        this.manage = manage;
    }

    public RMap<String, Object> getContain(String group) {
        return this.client.getMap(group);
    }
}
