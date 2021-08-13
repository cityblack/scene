package com.lzh.game.scene.common;

import com.lzh.game.scene.common.connect.server.*;
import com.lzh.game.scene.common.connect.sofa.SofaServer;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

public class CommonApp {

    public static void main(String[] args) throws InterruptedException {
        ServerConfig config = new ServerConfig();
        config.setPort(8081);
        ConnectServer<ServerConfig> server = new SofaServer(config);
        server.startup();
        InvokeManage invokeManage = server.invokeManage();
        final Hello demo = new Hello();
        invokeManage.registerInvoke(10086, new MethodInvoke() {
            @Override
            public Object invoke(Object... param) throws InvokeException, InvocationTargetException, IllegalAccessException {
                return demo.hello((String) param[0]);
            }

            @Override
            public Class<?>[] params() {
                return new Class[]{String.class};
            }

            @Override
            public int extParamIndex() {
                return 0;
            }
        });
    }

    public static class Hello {

        public String hello(String msg) {
            System.out.println("client say:" + msg);
            return "i am server!!";
        }
    }
}
