package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;

public interface RequestHandler {

    void dispatch(Connect connect, Request request);
}
