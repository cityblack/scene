package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.CustomSerializerManager;

import java.util.concurrent.atomic.AtomicBoolean;

public final class SofaRpcSerializationRegister {

    private static final SofaRpcSerialization RPC_SERIALIZATION = new SofaRpcSerialization();

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
//        if (CustomSerializerManager.getCustomSerializer(SofaRequest.class.getName()) == null) {
//            CustomSerializerManager.registerCustomSerializer(SofaRequest.class.getName(),
//                    RPC_SERIALIZATION);
//        }
//        if (CustomSerializerManager.getCustomSerializer(SofaResponse.class.getName()) == null) {
//            CustomSerializerManager.registerCustomSerializer(SofaResponse.class.getName(),
//                    RPC_SERIALIZATION);
//        }
    }
}
