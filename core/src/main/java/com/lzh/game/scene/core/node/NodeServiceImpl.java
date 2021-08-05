package com.lzh.game.scene.core.node;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.ConnectServer;
import com.lzh.game.scene.common.connect.sofa.SofaSceneConnect;
import com.lzh.game.scene.core.node.NodeService;

public class NodeServiceImpl implements NodeService {

    private ConnectServer connectServer;

    @Override
    public void register(Connect connect, NodeType type) {
        String key = SceneConnect.TO_UNIQUE.apply(connect.key(), type);
        SceneConnect sceneConnect = new SofaSceneConnect(connect, type, key);
        connectServer.manage().putSceneConnect(key, sceneConnect);
    }
}
