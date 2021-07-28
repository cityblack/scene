package com.lzh.game.scene.common.connect;

public class Request {

    private int id;

    private Object param;

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

    public static Request of(int id) {
        Request request = new Request();
        request.setId(id);
        return request;
    }
}
