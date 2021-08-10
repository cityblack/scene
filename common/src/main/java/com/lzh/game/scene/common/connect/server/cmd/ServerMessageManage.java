package com.lzh.game.scene.common.connect.server.cmd;

import com.lzh.game.scene.common.connect.server.InvokeManage;
import com.lzh.game.scene.common.connect.server.MethodInvoke;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerMessageManage implements CmdClassManage, InvokeManage {

    // 可能会动态加载
    private Map<Integer, Class<?>> clazz = new ConcurrentHashMap<>();

    private Map<Integer, MethodInvoke> invokes = new ConcurrentHashMap<>();

    @Override
    public Class<?> findClass(int cmd) {
        return this.clazz.get(cmd);
    }

    @Override
    public void registerClass(int cmd, Class<?> clazz) {
        this.clazz.put(cmd, clazz);
    }

    @Override
    public boolean existClass(int cmd) {
        return this.clazz.containsKey(cmd);
    }

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
