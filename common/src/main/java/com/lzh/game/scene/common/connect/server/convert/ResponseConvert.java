package com.lzh.game.scene.common.connect.server.convert;

import com.lzh.game.scene.common.connect.Request;

public class ResponseConvert extends AbstractRequestParamConvert {

    @Override
    public Object doConvert(Request request) {
        return request.getContext().getResponse();
    }
}
