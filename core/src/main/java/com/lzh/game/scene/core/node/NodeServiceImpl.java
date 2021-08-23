package com.lzh.game.scene.core.node;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.ConnectServer;
import com.lzh.game.scene.common.connect.sofa.SofaSceneConnect;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.common.proto.NodeInfoRequest;
import com.lzh.game.scene.core.node.NodeService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodeServiceImpl implements NodeService {

    private ConnectServer connectServer;

    public void setConnectServer(ConnectServer connectServer) {
        this.connectServer = connectServer;
    }

    @Override
    public List<NodeInfo> register(Connect connect, NodeInfoRequest request) {
        NodeType type = request.getType();
        String key = SceneConnect.TO_UNIQUE.apply(connect.key(), type);
        SceneConnect sceneConnect = new SofaSceneConnect(connect, type, key);
        connectServer.manage().putSceneConnect(key, sceneConnect);
        if (type == NodeType.SCENE_NODE) {
            return Collections.EMPTY_LIST;
        }
        return getSceneNode()
                .map(e -> {
                   NodeInfo info = new NodeInfo();
                   info.setKey(e.key());
                   info.setIp(e.host());
                   info.setPort(e.port());
                   return info;
                }).collect(Collectors.toList());
    }

    @Override
    public Stream<SceneConnect> getApiNode() {
        return Stream.concat(getNode(NodeType.CLIENT_NODE).stream()
                , getNode(NodeType.CLIENT_NODE_AND_SCENE_NODE).stream());
    }

    @Override
    public Stream<SceneConnect> getSceneNode() {
        return Stream.concat(getNode(NodeType.SCENE_NODE).stream()
                , getNode(NodeType.CLIENT_NODE_AND_SCENE_NODE).stream());
    }

    private List<SceneConnect> getNode(NodeType type) {
        return connectServer.manage().getByNode(type);
    }
}
