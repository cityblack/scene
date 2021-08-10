package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.CustomSerializerManager;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.common.connect.server.cmd.CmdClassManage;

import java.util.concurrent.atomic.AtomicBoolean;

public final class SofaRpcSerializationRegister {

    private static final SofaRpcSerializer RPC_SERIALIZATION = new SofaRpcSerializer();

    private static volatile AtomicBoolean registered = new AtomicBoolean(false);

    public static void registerCustomSerializer() {
        if (registered.compareAndSet(false, true)) {
            innerRegisterCustomSerializer();
        }
    }

    /**
     * we can override or rewrite the method
     */
    protected static void innerRegisterCustomSerializer() {
        // 注册序列化器到bolt
        if (CustomSerializerManager.getCustomSerializer(Request.class.getName()) == null) {
            CustomSerializerManager.registerCustomSerializer(Request.class.getName(),
                    RPC_SERIALIZATION);
        }
        if (CustomSerializerManager.getCustomSerializer(Response.class.getName()) == null) {
            CustomSerializerManager.registerCustomSerializer(Response.class.getName(),
                    RPC_SERIALIZATION);
        }
    }

    public static void setSerialization(Serializer serializer) {
        RPC_SERIALIZATION.setSerializer(serializer);
    }

    public static void setClassManage(CmdClassManage classManage) {
        RPC_SERIALIZATION.setClassManage(classManage);
    }
}
