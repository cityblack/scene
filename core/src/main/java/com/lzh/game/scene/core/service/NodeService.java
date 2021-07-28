package com.lzh.game.scene.core.service;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;

/**
 * 注册的节点管理器 当连接上来之后 需要主动的
 */
public interface NodeService {

    void register(Connect connect, NodeType type);
}
