package com.lzh.game.scene.core.node;

import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.common.proto.NodeInfoRequest;

import java.util.List;
import java.util.stream.Stream;

/**
 * 注册的节点管理器 当连接上来之后 需要主动的通知自己的类型
 * 元数据
 */
public interface NodeService {

    /**
     * 将节点进行类型注册
     * @param connect
     * @param request
     */
    List<NodeInfo> register(Connect connect, NodeInfoRequest request);

    Stream<SceneConnect> getSceneNode();

    Stream<SceneConnect> getApiNode();
}
