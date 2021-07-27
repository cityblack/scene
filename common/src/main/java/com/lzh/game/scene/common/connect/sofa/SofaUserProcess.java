package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.lzh.game.scene.common.connect.Request;

public class SofaUserProcess extends AsyncUserProcessor<Object> {

    static {
       SofaRpcSerializationRegister.registerCustomSerializer();
    }


    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, Object request) {

    }

    @Override
    public String interest() {
        return null;
    }
}
