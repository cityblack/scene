package com.lzh.game.scene.common.connect;

public class Response extends ExchangeBase {

    public static Response of(int cmd, Object param) {
        Response response = new Response();
        response.setId(cmd);
        response.setParam(param);
        response.setClassName(param.getClass().getName());
        return response;
    }
}
