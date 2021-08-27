package com.lzh.game.scene.common.connect.server.convert;

import com.lzh.game.scene.common.connect.Request;

public class ConnectConvert extends AbstractRequestParamConvert
        implements RequestParamConvert {

    @Override
    public Object doConvert(Request request) {
        return request.getContext().getConnect();
    }
}
