package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;

public interface RequestHandler {

    Response dispatch(Request request);
}
