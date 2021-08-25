package com.lzh.game.scene.core.node;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.server.ServerConfig;
import com.lzh.game.scene.common.connect.sofa.SofaServer;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.core.RedissonClientUtils;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;
import java.net.URL;

class NodeServiceTest {

    private RedissonClient client = RedissonClientUtils.getInstance();

    private NodeService nodeService = new RedisNodeServiceImpl(new SofaServer(new ServerConfig()), client);

    @Test
    void register() throws InterruptedException {
        String key = NodeType.SCENE_NODE.getName() + "127.0.0.1:8081";
        NodeInfo info = new NodeInfo();
        info.setIp("127.0.0.1");
        info.setPort(8081);
        info.setKey(key);
        client.getMap(NodeType.SCENE_NODE.getName()).put(key, info);
    }

    @Test
    void getSceneNode() {
        nodeService.getSceneNode().forEach(System.out::println);
    }

    @Test
    void getApiNode() {
    }
}
