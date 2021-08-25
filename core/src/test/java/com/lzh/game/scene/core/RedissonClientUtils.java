package com.lzh.game.scene.core;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class RedissonClientUtils {

    private static RedissonClient client;

    static {
        try {
            URL url = RedissonClientUtils.class.getClassLoader().getResource("redisson.yml");
            Config config = Config.fromYAML(new File(url.getFile()));
            client = Redisson.create(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RedissonClientUtils() {}

    public static RedissonClient getInstance() {
        return client;
    }
}
