package com.lzh.game.scene.common.connect.server;

public interface Invoke {

    Object invoke(Object... param) throws InvokeException;
}
