package com.lzh.game.scene.common.connect;

public class Response {

    private String error;

    private Object param;

    private RequestContext context;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public RequestContext getContext() {
        return context;
    }

    public void setContext(RequestContext context) {
        this.context = context;
    }

    public static Response of(Object param) {
        Response response = new Response();
        response.setParam(param);
        return response;
    }
}
