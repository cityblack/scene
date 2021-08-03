package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.RpcServer;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.lzh.game.scene.common.ContextDefined;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.RequestContext;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.server.ConnectServer;
import com.lzh.game.scene.common.connect.server.MethodInvoke;
import com.lzh.game.scene.common.connect.server.InvokeException;
import com.lzh.game.scene.common.connect.server.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class SofaUserProcess extends AsyncUserProcessor<Request> {

    private static final Logger logger = LoggerFactory.getLogger(SofaUserProcess.class);

    private static final String INTEREST = Request.class.getName();

    private RequestHandler handler;

    public SofaUserProcess(RequestHandler requestHandler) {
        this.handler = requestHandler;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, Request request) {

        try {
            RequestContext requestContext = new RequestContext();
            requestContext.setAttr(ContextDefined.SOFA_ASYNC_CONTEXT, asyncCtx);
            requestContext.setAttr(ContextDefined.SOFA_CONNECT_REQUEST, bizCtx.getConnection());

            request.setContext(requestContext);

            Response response = handler.dispatch(request);

            if (bizCtx.isRequestTimeout()) {
                response.setError("Request timeout!!");
                asyncCtx.sendResponse(response);
                return;
            }

            if (Objects.nonNull(response.getParam()) || Objects.nonNull(response.getError())) {
                asyncCtx.sendResponse(response);
            }

        } catch (Exception e) {
            logger.error("Request error!!", e);
            Response response = new Response();
            response.setError(e.getMessage());
            asyncCtx.sendResponse(response);
        }
    }

    @Override
    public String interest() {
        return INTEREST;
    }

    @Override
    public ExecutorSelector getExecutorSelector() {
        // todo 将jfra的线程和工作线程分开
        return super.getExecutorSelector();
    }
}
