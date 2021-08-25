package com.lzh.game.scene.core.service;

import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.RedissonClientUtils;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;

class SceneServiceTest {

    private RedissonClient client = RedissonClientUtils.getInstance();

    private String TOP_TEST = "top_test";

    @Test
    public void test() {
        client
                .getTopic(TOP_TEST)
                .addListener(SceneInstance.class, new MessageListener<SceneInstance>() {
            @Override
            public void onMessage(CharSequence channel, SceneInstance msg) {
                System.out.println("get first " + msg);
            }
        });

        client
                .getTopic(TOP_TEST)
                .addListener(SceneInstance.class, new MessageListener<SceneInstance>() {
                    @Override
                    public void onMessage(CharSequence channel, SceneInstance msg) {
                        System.out.println("get second " + msg);
                    }
                });
        SceneInstance instance = new SceneInstance();
        instance.setGroup("xx");
        instance.setMap(1);
        instance.setUnique("xx-1");
        client.getTopic(TOP_TEST).publish(instance);
    }
}
