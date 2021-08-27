package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.Request;

public interface RequestHelper {

    Object[] paramConvert(Request request, MethodInvoke methodInvoke);

}
