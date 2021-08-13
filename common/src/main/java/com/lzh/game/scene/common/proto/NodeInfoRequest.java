package com.lzh.game.scene.common.proto;

import com.lzh.game.scene.common.NodeType;

public class NodeInfoRequest {

    private NodeType type;

    private int weight;

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
