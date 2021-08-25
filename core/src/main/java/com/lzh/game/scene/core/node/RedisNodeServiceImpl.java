package com.lzh.game.scene.core.node;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import com.lzh.game.scene.common.connect.sofa.SofaSceneConnect;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.common.proto.NodeInfoRequest;
import org.redisson.api.RedissonClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lzh.game.scene.common.ContextConstant.SCENE_CONNECT_RELATION;

public class RedisNodeServiceImpl implements NodeService {

    private AbstractServerBootstrap<?> bootstrap;

    private RedissonClient client;

    public RedisNodeServiceImpl(AbstractServerBootstrap<?> bootstrap, RedissonClient client) {
        this.bootstrap = bootstrap;
        this.client = client;
    }

    @Override
    public List<NodeInfo> register(Connect connect, NodeInfoRequest request) {
        NodeType type = request.getType();
        String key = SceneConnect.TO_UNIQUE.apply(connect.key(), type);
        SceneConnect sceneConnect = new SofaSceneConnect(connect, type, key);
        bootstrap.getConnectManage().putConnect(key, sceneConnect);
        NodeInfo info = new NodeInfo();
        info.setKey(key);
        info.setIp(sceneConnect.host());
        info.setPort(sceneConnect.port());

        client.getMap(type.getName()).put(key, info);
        if (type == NodeType.SCENE_NODE) {
            return Collections.EMPTY_LIST;
        }
        return getSceneNode()
                .collect(Collectors.toList());
    }

    @Override
    public Stream<NodeInfo> getApiNode() {

        return Stream.concat(getNode(NodeType.CLIENT_NODE)
                , getNode(NodeType.CLIENT_NODE_AND_SCENE_NODE));
    }

    @Override
    public void deregister(Connect connect) {
        // 为空说明未注册过
        String value = connect.getAttr(SCENE_CONNECT_RELATION);
        if (Objects.isNull(value)) {
            return;
        }
        SceneConnect sceneConnect = bootstrap.getConnectManage().getConnect(value);
        if (Objects.nonNull(sceneConnect)) {
            NodeType type = sceneConnect.type();
            client.getMap(type.getName()).remove(value);
        }
    }

    @Override
    public Stream<NodeInfo> getSceneNode() {
        return Stream.concat(getNode(NodeType.SCENE_NODE)
                , getNode(NodeType.CLIENT_NODE_AND_SCENE_NODE));
    }

    private Stream<NodeInfo> getNode(NodeType type) {
        return client
                .getMap(type.getName())
                .values()
                .stream()
                .map(e -> (NodeInfo)e);
    }
}
