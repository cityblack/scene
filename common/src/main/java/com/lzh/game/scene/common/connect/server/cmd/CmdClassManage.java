package com.lzh.game.scene.common.connect.server.cmd;

/**
 * 序列化方案决定需要使用
 */
public interface CmdClassManage {

    Class<?> findClass(int cmd);

    void registerClass(int cmd, Class<?> clazz);

    boolean existClass(int cmd);
}
