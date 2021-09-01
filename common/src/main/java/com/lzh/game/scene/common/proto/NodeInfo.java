package com.lzh.game.scene.common.proto;

public class NodeInfo {

    private String key;

    private String ip;

    private int port;

    private int type;

    private int status;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "key='" + key + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", type=" + type +
                '}';
    }
}
