package com.lzh.game.scene.common.connect.server.convert;

import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;

import java.util.Objects;

public class SceneConnectConvert extends AbstractRequestParamConvert {

    @Override
    public Object doConvert(Request request) {
        Connect connect = request.getContext().getConnect();
        if (Objects.isNull(connect)) {
            return null;
        }
        return connect.getAttr(ContextConstant.SCENE_CONNECT_RELATION);
    }
}
