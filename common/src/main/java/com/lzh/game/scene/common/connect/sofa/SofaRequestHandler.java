package com.lzh.game.scene.common.connect.sofa;

import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class SofaRequestHandler implements RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(SofaRequestHandler.class);

    private InvokeManage invokeManage;

    private RequestHelper requestHelper;

    public SofaRequestHandler(InvokeManage invokeManage, RequestHelper helper) {
        this.invokeManage = invokeManage;
        this.requestHelper = helper;
    }

    @Override
    public Response dispatch(Request request) {
        Response response = new Response();
        response.setContext(request.getContext());
        request.getContext().setResponse(response);

        MethodInvoke invoke = invokeManage.findInvoke(request.getId());
        if (Objects.isNull(invoke)) {
            response.setError("Illegal request, Not defined cmd " + request.getId());
            return response;
        }
        try {
            doInvoke(request, response, invoke);
        } catch (Exception e) {
            logger.error("Request error!", e);
            response.setError(e.getMessage());
        }
        return response;
    }

    private void doInvoke(Request request, Response response, MethodInvoke invoke) throws InvokeException, InvocationTargetException, IllegalAccessException {
        Class<?>[] clazz = invoke.params();
        if (clazz.length == 0) {
            Object o = invoke.invoke();
            setParam(response, o);
        } else {
            Object[] params = requestHelper.paramConvert(request, invoke);
            Object o = invoke.invoke(params);
            setParam(response, o);
        }
    }

    private void setParam(Response response, Object o) {
        if (!(o instanceof Void)) {
            response.setParam(o);
        }
    }


}
