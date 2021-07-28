package com.lzh.game.scene.common.connect.server;

import org.springframework.ui.Model;

/**
 * 协议具体类型绑定关系管理器
 * 使用这个接口去绑定请求参数类型是为了减少带宽，请求的时候不需要将对应类型的请求带上，用cmd直接进行区分
 */
public interface CmdManage {

    boolean put(int cmd, CmdModel cmdModel);

    boolean contain(int cmd);

    Class<?> getClass(int cmd);

    Model get(int cmd);

    Invoke getInvoke(int cmd);

}
