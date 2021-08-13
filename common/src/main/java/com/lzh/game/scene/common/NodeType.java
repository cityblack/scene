package com.lzh.game.scene.common;

public enum NodeType {
    // 管理中心节点
    SCENE_MANAGE_NODE("manage-node-"),
    // 客户端节点
    CLIENT_NODE("client-node-"),
    // 场景节点
    SCENE_NODE("scene-node-"),
    // 客户端节点和场景节点
    CLIENT_NODE_AND_SCENE_NODE("client-and-scene-node-")
    ;

    private String name;

    NodeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean inRange(int index) {
        if (index < 0) {
            return false;
        }
        return index < values().length;
    }

    public static boolean isManageNode(int index) {
        return index == SCENE_MANAGE_NODE.ordinal();
    }
}
