package com.lzh.game.scene.common.connect;

public class Response {

    private String className;

    private String error;

    private Object param;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public static Response of(Object param) {
        Response response = new Response();
        response.setParam(param);
        response.setClassName(param.getClass().getName());
        return response;
    }
}
