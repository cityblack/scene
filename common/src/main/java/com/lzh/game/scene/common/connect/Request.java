package com.lzh.game.scene.common.connect;

public class Request extends ExchangeBase {

    public static Request of(int id, Object param) {
        Request request = new Request();
        request.setId(id);
        request.setParam(param);
        request.setClassName(param.getClass().getName());
        return request;
    }
}
