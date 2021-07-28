package com.lzh.game.scene.common.connect.server;

public class CmdModel {

    private Class<?> requestParamType;

    private Invoke requestInvoke;

    public Class<?> getRequestParamType() {
        return requestParamType;
    }

    public void setRequestParamType(Class<?> requestParamType) {
        this.requestParamType = requestParamType;
    }

    public Invoke getRequestInvoke() {
        return requestInvoke;
    }

    public void setRequestInvoke(Invoke requestInvoke) {
        this.requestInvoke = requestInvoke;
    }
}
