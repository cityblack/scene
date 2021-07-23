package com.lzh.game.scene.common;

public enum NodeType {
    // 管理中心节点
    SCENE_MANAGE_NODE("com.lzh.game.scene.manage.node."),
    // 客户端节点
    CLIENT_NODE("com.lzh.game.client.node."),
    // 场景节点
    SCENE_NODE("com.lzh.game.scene.node.")

    ;

    private String name;

    NodeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
