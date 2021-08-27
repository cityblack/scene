package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.Connection;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.connect.*;
import com.lzh.game.scene.common.connect.server.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.lzh.game.scene.common.ContextConstant.ERROR_COMMON_RESPONSE;
import static com.lzh.game.scene.common.ContextConstant.SOURCE_CONNECT_RELATION;

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
            if (isTimeout(request, bizCtx, asyncCtx, Response.of())) {
                return;
            }
            Connection connection = bizCtx.getConnection();
            RequestContext requestContext = new RequestContext();
            requestContext.setAttr(ContextConstant.SOFA_ASYNC_CONTEXT, asyncCtx);
            requestContext.setAttr(ContextConstant.SOFA_CONNECT_REQUEST, connection);
            Connect connect = (Connect) connection.getAttribute(SOURCE_CONNECT_RELATION);
            requestContext.setConnect(connect);
            request.setContext(requestContext);

            Response<?> response = handler.dispatch(request);
            if (isTimeout(request, bizCtx, asyncCtx, response)) {
                return;
            }
            byte status = response.getStatus();
            if (status != ContextConstant.RIGHT_RESPONSE) {
                logger.error("Request error:", response.getError());
            }
            if (!request.isOneWay()) {
                asyncCtx.sendResponse(response);
            }
        } catch (Exception e) {
            logger.error("Request error!!", e);
            if (!request.isOneWay()) {
                Response<?> response = Response.of();
                response.setErrorInfo(ERROR_COMMON_RESPONSE, e.getMessage());
                asyncCtx.sendResponse(response);
            }
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

    private boolean isTimeout(Request request, BizContext bizCtx, AsyncContext asyncCtx, Response<?> response) {
        if (request.isOneWay()) {
            return false;
        }
        if (bizCtx.isRequestTimeout()) {
            response.setErrorInfo(ERROR_COMMON_RESPONSE, "Request timeout!!");
            asyncCtx.sendResponse(response);
            return true;
        }
        return false;
    }
}
