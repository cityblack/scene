package com.lzh.game.scene.api;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;

public class SimpleServerUserProcessor extends SyncUserProcessor<RequestBody> {

    @Override
    public Object handleRequest(BizContext bizCtx, RequestBody request) throws Exception {
        ResponseBody body = new ResponseBody();
        body.setId(request.getId());
        body.setMsg("我收到了" + request.getMsg());
        return body;
    }

    @Override
    public String interest() {
        return "com.lzh.game.scene.api.RequestBody";
    }
}
