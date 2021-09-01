package com.lzh.game.scene.core.node;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.RequestSpace;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import com.lzh.game.scene.common.connect.sofa.SofaSceneConnect;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.common.proto.NodeInfoRequest;
import com.lzh.game.scene.core.jrfa.ReplicatorCmd;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lzh.game.scene.common.ContextConstant.SCENE_CONNECT_RELATION;

public class RedisNodeServiceImpl implements NodeService {

    private static final Logger logger = LoggerFactory.getLogger(RedisNodeServiceImpl.class);

    private AbstractServerBootstrap<?> bootstrap;

    private RedissonClient client;

    public RedisNodeServiceImpl(AbstractServerBootstrap<?> bootstrap, RedissonClient client) {
        this.bootstrap = bootstrap;
        this.client = client;
    }

    @Override
    public List<NodeInfo> register(Connect connect, NodeInfoRequest request) {
        NodeType type = NodeType.values()[request.getType()];

        String key = SceneConnect.TO_UNIQUE.apply(connect.key(), type);
        SceneConnect sceneConnect = new SofaSceneConnect(connect, type, key);
        logger.info("Connect {} bind {} {}", connect.key(), key, bootstrap.getConfig().getPort());
        bootstrap.getConnectManage().putConnect(key, sceneConnect);

        NodeInfo info = new NodeInfo();
        info.setKey(key);
        info.setIp(sceneConnect.host());
        info.setPort(request.getPort());
        info.setType(request.getType());

        client.getMap(type.getName()).put(key, info);

        if (NodeType.isSceneNode(type)) {
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
        SceneConnect sceneConnect = connect.getAttr(SCENE_CONNECT_RELATION);
        if (Objects.isNull(sceneConnect)) {
            return;
        }
        NodeType type = sceneConnect.type();
        client.getMap(type.getName()).remove(sceneConnect.key());
    }

    @Override
    public void onNodeChange(NodeInfo info) {
        if (!NodeType.isSceneNode(info.getType())) {
            return;
        }
        Collection<SceneConnect> connects = bootstrap.getConnectManage().getAllConnect();
        for (SceneConnect connect : connects) {
            if (!NodeType.isClientNode(connect.type())) {
                continue;
            }
            Request request = Request.of(RequestSpace.cmd(RequestSpace.LISTEN_INSTANCE_SPACE
                    , RequestSpace.LISTEN_NODE_CHANGE), info);
            connect.sendOneWay(request);
        }
    }

    @Override
    public Stream<NodeInfo> getSceneNode() {
        return Stream.concat(getNode(NodeType.SCENE_NODE)
                , getNode(NodeType.CLIENT_NODE_AND_SCENE_NODE));
    }

    private Stream<NodeInfo> getNode(NodeType type) {
        return getContain(type)
                .readAllValues()
                .stream();
    }

    private RMap<String, NodeInfo> getContain(NodeType type) {
        return this.client
                .getMap(type.getName());
    }
}
