package com.lzh.game.scene.common.connect;

public class Request {

    private int id;

    private Object param;

    private RequestContext context;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static Request of(int id) {
        Request request = new Request();
        request.setId(id);
        return request;
    }
}
