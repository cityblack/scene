package com.lzh.game.scene.common.connect.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerMessageManage implements CmdClassManage, InvokeManage {

    // 可能会动态加载
    private Map<Integer, Class<?>> clazz = new ConcurrentHashMap<>();

    private Map<Integer, MethodInvoke> invokes = new ConcurrentHashMap<>();

    @Override
    public Class<?> findClass(int cmd) {
        return null;
    }

    @Override
    public void registerClass(int cmd, Class<?> clazz) {

    }

    @Override
    public boolean existClass(int cmd) {
        return false;
    }

    @Override
    public MethodInvoke findInvoke(int cmd) {
        return null;
    }

    @Override
    public void registerInvoke(int cmd, MethodInvoke methodInvoke) {

    }

    @Override
    public boolean existInvoke(int cmd) {
        return false;
    }
}
