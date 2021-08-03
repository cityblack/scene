package com.lzh.game.scene.core;

import com.lzh.game.scene.common.connect.server.ServerConfig;

import java.util.ArrayList;
import java.util.List;

public class ClusterServerConfig extends ServerConfig {

    private String ip = "localhost";

    private String metaUri;

    private String consistLogUri;

    private List<String> cluster = new ArrayList<>();

    public List<String> getCluster() {
        return cluster;
    }

    public String getConsistLogUri() {
        return consistLogUri;
    }

    public void setConsistLogUri(String consistLogUri) {
        this.consistLogUri = consistLogUri;
    }

    public void setCluster(List<String> cluster) {
        this.cluster = cluster;
    }

    public String getMetaUri() {
        return metaUri;
    }

    public void setMetaUri(String metaUri) {
        this.metaUri = metaUri;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
