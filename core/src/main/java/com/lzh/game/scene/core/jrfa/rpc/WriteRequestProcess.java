package com.lzh.game.scene.core.jrfa.rpc;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.alipay.sofa.jraft.Node;
import com.lzh.game.scene.core.jrfa.JRService;
import com.lzh.game.scene.core.jrfa.rpc.entity.Response;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class WriteRequestProcess extends AsyncUserProcessor<WriteRequest> {

    private static final Logger logger = LoggerFactory.getLogger(WriteRequestProcess.class);

    private static final String INTEREST = WriteRequest.class.getName();

    private JRService jrService;

    public WriteRequestProcess(JRService jrService) {
        this.jrService = jrService;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, WriteRequest request) {

        Response.Builder response = Response.newBuilder();
        response.setSuccess(true);

        if (bizCtx.isRequestTimeout()) {
            response.setErrMsg("request timeout!!");
            response.setSuccess(false);
            asyncCtx.sendResponse(response);
        }
        Node node = jrService.node();
        if (!node.isLeader()) {
            response.setErrMsg("can't find leader!!");
            response.setSuccess(false);
            sendResponse(asyncCtx, response);
        } else {
            CompletableFuture<Void> future = new CompletableFuture<>();
            response.setSuccess(true);

            jrService.leaderWriteInvoke(request, future);
            future.exceptionally(throwable -> {
                response.setErrMsg(throwable.getMessage());
                response.setSuccess(false);
                sendResponse(asyncCtx, response);
                return null;
            }).thenAccept(v -> sendResponse(asyncCtx, response));
        }
    }

    private void sendResponse(AsyncContext asyncContext, Response.Builder response) {
        try {
            asyncContext.sendResponse(response.build());
        } catch (Exception e) {
            logger.error("Response error:", e);
        }
    }

    @Override
    public String interest() {
        return INTEREST;
    }
}
