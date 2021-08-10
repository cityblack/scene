package com.lzh.game.scene.common;

import com.lzh.game.scene.common.connect.server.*;
import com.lzh.game.scene.common.connect.server.cmd.CmdClassManage;
import com.lzh.game.scene.common.connect.sofa.SofaServer;

import java.lang.reflect.Method;

public class CommonApp {

    public static void main(String[] args) {
        ServerConfig config = new ServerConfig();
        config.setPort(8181);
        ConnectServer<ServerConfig> server = new SofaServer(config);
        server.startup();
        CmdClassManage classManage = server.classManage();
        classManage.registerClass(10086, String.class);
        InvokeManage invokeManage = server.invokeManage();
    }
}
