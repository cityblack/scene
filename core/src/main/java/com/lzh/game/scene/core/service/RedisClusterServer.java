package com.lzh.game.scene.core.service;

import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.core.ClusterServerConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;
import java.io.InputStream;

public class RedisClusterServer<T extends ClusterServerConfig> extends SofaClusterServer<T> {

    private RedissonClient client;

    public RedisClusterServer(T config) {
        super(config);
    }

    @Override
    public RpcServer doInit(T config) {
        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream("redisson.yml");
            Config redisConfig = Config.fromYAML(stream);
            RedissonClient redisson = Redisson.create(redisConfig);
            this.client = redisson;
        } catch (IOException e) {
            throw new NullPointerException("redisson config is null");
        }

        return new RpcServer(config.getPort());
    }
}
