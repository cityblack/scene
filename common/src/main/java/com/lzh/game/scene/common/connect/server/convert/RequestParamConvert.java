package com.lzh.game.scene.common.connect.server.convert;

import com.lzh.game.scene.common.connect.Request;

/**
 * 内置参数转换接口
 */
public interface RequestParamConvert {

    Object convert(Request request);
}
