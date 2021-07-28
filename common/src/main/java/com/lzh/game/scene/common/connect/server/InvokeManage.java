package com.lzh.game.scene.common.connect.server;

public interface InvokeManage {

    MethodInvoke findInvoke(int cmd);

    void registerInvoke(int cmd, MethodInvoke methodInvoke);

    boolean existInvoke(int cmd);
}
