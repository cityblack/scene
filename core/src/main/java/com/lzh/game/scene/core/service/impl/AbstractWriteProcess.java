package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;

public abstract class AbstractWriteProcess {

    public abstract void onRequest(WriteRequest request);
}
