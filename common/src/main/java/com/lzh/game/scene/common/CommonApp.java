package com.lzh.game.scene.common;

import com.lzh.game.scene.common.connect.server.*;
import com.lzh.game.scene.common.connect.server.cmd.Action;
import com.lzh.game.scene.common.connect.server.cmd.Cmd;
import com.lzh.game.scene.common.connect.sofa.SofaServer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class CommonApp {

    public static void main(String[] args) throws InterruptedException {
        ServerConfig config = new ServerConfig();
        config.setPort(8081);
        SofaServer<ServerConfig> server = new SofaServer(config);
        server.startup();

        final Hello demo = new Hello();
        server.addCmdTarget(Arrays.asList(demo));
    }

    @Action
    public static class Hello {

        @Cmd(1)
        public String hello(String msg) {
            System.out.println("client say:" + msg);
            return "i am server!!";
        }
    }
}
