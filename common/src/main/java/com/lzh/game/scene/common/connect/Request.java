package com.lzh.game.scene.common.connect;

public class Request {

    private int id;

    private Object param;
    // 请求类型 请勿额外设置
    private byte type;

    private String paramClassName;

    private Class<?> paramClass;

    // ======
    private transient RequestContext context;

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

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getParamClassName() {
        return paramClassName;
    }

    public void setParamClassName(String paramClassName) {
        this.paramClassName = paramClassName;
    }

    public Class<?> getParamClass() {
        return paramClass;
    }

    public void setParamClass(Class<?> paramClass) {
        this.paramClass = paramClass;
    }

    public static Request of(int id, Object param) {
        Request request = new Request();
        request.id = id;
        request.param = param;
        request.paramClass = param.getClass();
        request.paramClassName = param.getClass().getName();
        return request;
    }

    public static Request of(int id) {
        Request request = new Request();
        request.id = id;
        return request;
    }
}
