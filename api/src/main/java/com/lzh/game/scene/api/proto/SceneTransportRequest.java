package com.lzh.game.scene.api.proto;

public class SceneTransportRequest {

    private int strategy;

    private byte[] requestBody;

    private String bodyClassName;

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public byte[] getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(byte[] requestBody) {
        this.requestBody = requestBody;
    }

    public String getBodyClassName() {
        return bodyClassName;
    }

    public void setBodyClassName(String bodyClassName) {
        this.bodyClassName = bodyClassName;
    }
}
