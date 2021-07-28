package com.lzh.game.scene.common.connect.server;

public interface CmdClassManage {

    Class<?> findClass(int cmd);

    void registerClass(int cmd, Class<?> clazz);

    boolean existClass(int cmd);
}
