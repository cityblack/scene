package com.lzh.game.scene.common.connect.server.cmd;

import com.lzh.game.scene.common.connect.server.InvokeManage;
import com.lzh.game.scene.common.connect.server.MethodInvoke;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerMessageManage implements InvokeManage {

    private Map<Integer, MethodInvoke> invokes = new ConcurrentHashMap<>();

    @Override
    public MethodInvoke findInvoke(int cmd) {
        return this.invokes.get(cmd);
    }

    @Override
    public void registerInvoke(int cmd, MethodInvoke methodInvoke) {
        this.invokes.put(cmd, methodInvoke);
    }

    @Override
    public boolean existInvoke(int cmd) {
        return this.invokes.containsKey(cmd);
    }
}
