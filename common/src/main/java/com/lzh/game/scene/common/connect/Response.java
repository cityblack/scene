package com.lzh.game.scene.common.connect;

import java.util.Objects;

public class Response {

    private byte status;

    private String error;

    private Object param;

    private String paramClassName;

    private Class<?> paramClass;
    // === 非传递属性
    private transient RequestContext context;

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

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public static Response of() {
        return new Response();
    }

    public void setParamWithType(Object o) {
        this.param = o;
        if (Objects.nonNull(this.param)) {
            this.paramClass = this.param.getClass();
            this.paramClassName = this.param.getClass().getName();
        }
    }
}
