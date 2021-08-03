package com.lzh.game.scene.common;

import com.lzh.game.scene.common.connect.server.*;
import com.lzh.game.scene.common.connect.sofa.SofaServer;

import java.lang.reflect.Method;

public class CommonApp {

    public void hello(String say) {
        System.out.println(say);
    }

    public static void main(String[] args) {
        CommonApp app = new CommonApp();
        ServerConfig config = new ServerConfig();
        config.setPort(8181);
        ConnectServer<ServerConfig> server = new SofaServer(config);
        server.start();
        CmdClassManage classManage = server.classManage();
        classManage.registerClass(10086, String.class);
        InvokeManage invokeManage = server.invokeManage();
        app.putInvoke(invokeManage);
    }

    public void putInvoke(InvokeManage invokeManage) {
        Class<?> clazz = this.getClass();
        try {
            Method method = clazz.getMethod("hello", String.class);
            MethodInvoke invoke = new MethodInvokeEndpoint(this, method, new Class[]{String.class});
            invokeManage.registerInvoke(10086, invoke);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
