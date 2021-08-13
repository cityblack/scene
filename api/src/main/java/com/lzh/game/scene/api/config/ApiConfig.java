package com.lzh.game.scene.api.config;

import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.BootstrapConfig;

import java.util.ArrayList;
import java.util.List;

public class ApiConfig implements BootstrapConfig {

    private List<Member> cluster = new ArrayList<>();
    // 当前场景类型
    private int nodeType = 1;

    private int requestOutTime = 5000;

    public List<Member> getCluster() {
        return cluster;
    }

    public void setCluster(List<Member> cluster) {
        this.cluster = cluster;
    }

    public void addMember(Member member) {
        this.cluster.add(member);
    }

    public int getRequestOutTime() {
        return requestOutTime;
    }

    public void setRequestOutTime(int requestOutTime) {
        this.requestOutTime = requestOutTime;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public void check() {
        if (NodeType.isManageNode(this.nodeType)) {
            throw new IllegalArgumentException("The node type can't set Manage node");
        }
        if (!NodeType.inRange(this.nodeType)) {
            throw new NullPointerException("The node type is null.");
        }
    }
}
