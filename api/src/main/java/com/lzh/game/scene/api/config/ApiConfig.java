package com.lzh.game.scene.api.config;

import java.util.ArrayList;
import java.util.List;

public class ApiConfig {

    private List<Member> cluster = new ArrayList<>();

    public List<Member> getCluster() {
        return cluster;
    }

    public void setCluster(List<Member> cluster) {
        this.cluster = cluster;
    }

    public void addMember(Member member) {
        this.cluster.add(member);
    }
}
