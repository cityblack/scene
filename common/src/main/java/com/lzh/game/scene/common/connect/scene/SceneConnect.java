package com.lzh.game.scene.common.connect.scene;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Connect;

import java.util.function.BiFunction;

/**
 * 场景的通信连接
 */
public interface SceneConnect extends Connect {

    NodeType type();

    /**
     * 由场景类型连接原来的key(ip+port)主成唯一标识
     * @return
     */
    String key();

    /**
     * 真实通信对象
     * @return
     */
    Connect bridge();

    BiFunction<String, NodeType, String> TO_UNIQUE = (key, type) -> type.getName() + key;
}
