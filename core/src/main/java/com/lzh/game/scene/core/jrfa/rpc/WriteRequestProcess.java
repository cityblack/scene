package com.lzh.game.scene.core.jrfa.rpc;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.alipay.sofa.jraft.Node;
import com.google.protobuf.ByteString;
import com.lzh.game.scene.core.jrfa.JRService;
import com.lzh.game.scene.core.jrfa.rpc.entity.Response;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;

import java.util.Objects;
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

        if (!node.isLeader()) {
            asyncCtx.sendResponse(buildResponse("can't find leader!!", null));
            return;
        }
        CompletableFuture<Response> future = new CompletableFuture<>();
        jrService.applyOperation(node, request, future);
//        future.thenAccept(response -> asyncCtx.sendResponse())
    }

    private Response buildResponse(String error, Object data) {
        Response.Builder builder = Response.newBuilder();
        builder.setErrMsg(error);
        if (Objects.nonNull(data)) {
            builder.setData(ByteString.copyFrom(jrService.serializer().encode(data)));
        }
        return builder.build();
    }

    @Override
    public String interest() {
        return INTEREST;
    }
}
