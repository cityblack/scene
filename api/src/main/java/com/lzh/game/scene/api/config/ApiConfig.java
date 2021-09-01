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
    // 当场景类型为场景节点时候生效
    private int port;
    // 连接场景节点失败尝试重新连接次数
    private int connectErrorRetryTimes = 3;

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

    public NodeType getDefinedNodeType() {
        return NodeType.values()[getNodeType()];
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectErrorRetryTimes() {
        return connectErrorRetryTimes;
    }

    public void setConnectErrorRetryTimes(int connectErrorRetryTimes) {
        this.connectErrorRetryTimes = connectErrorRetryTimes;
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
