package com.lzh.game.scene.core.jrfa.rpc;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.alipay.sofa.jraft.Node;
import com.lzh.game.scene.core.jrfa.JRService;
import com.lzh.game.scene.core.jrfa.rpc.entity.Response;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;

import java.util.concurrent.CompletableFuture;

public class WriteRequestProcess extends AsyncUserProcessor<WriteRequest> {

    private static final String INTEREST = WriteRequest.class.getName();

    private JRService jrService;

    public WriteRequestProcess(JRService jrService) {
        this.jrService = jrService;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, WriteRequest request) {

        Node node = jrService.node();
        Response.Builder response = Response.newBuilder();
        if (!node.isLeader()) {
            response.setErrMsg("can't find leader!!");
            response.setSuccess(false);
            asyncCtx.sendResponse(response);
        } else {
            CompletableFuture<Void> future = new CompletableFuture<>();
            response.setSuccess(true);

            jrService.leaderWriteInvoke(request, future);
            future.exceptionally(throwable -> {
                response.setErrMsg(throwable.getMessage());
                response.setSuccess(false);
                asyncCtx.sendResponse(response);
                return null;
            }).thenAccept(v -> asyncCtx.sendResponse(response));
        }
    }


    @Override
    public String interest() {
        return INTEREST;
    }
}
