package com.lzh.game.scene.core.service;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;

/**
 * 注册的节点管理器 当连接上来之后 需要主动的通知自己的类型
 * 元数据
 */
public interface NodeService {

    /**
     * 将节点进行类型注册
     * @param connect
     * @param type
     */
    void register(Connect connect, NodeType type);
}
