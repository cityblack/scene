package com.lzh.game.scene.common.connect.sofa;

import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class SofaRequestHandler implements RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(SofaRequestHandler.class);

    private InvokeManage invokeManage;

    public SofaRequestHandler(InvokeManage invokeManage) {
        this.invokeManage = invokeManage;
    }

    @Override
    public Response dispatch(Request request) {
        Response response = new Response();

        MethodInvoke invoke = invokeManage.findInvoke(request.getId());
        if (Objects.isNull(invoke)) {
            response.setError("Illegal request, Not defined cmd " + request.getId());
            return response;
        }
        try {
            doInvoke(request, response, invoke);
        } catch (InvokeException e) {
            logger.error("Request error!", e);
            response.setError(e.getMessage());
        }
        return response;
    }

    private void doInvoke(Request request, Response response, MethodInvoke invoke) throws InvokeException {
        Class<?>[] clazz = invoke.params();
        if (clazz.length == 0) {
            Object o = invoke.invoke();
            setParam(response, o);
        } else {
            Object[] params = convertParam(clazz, request);
            Object o = invoke.invoke(params);
            setParam(response, o);
        }
    }

    private void setParam(Response response, Object o) {
        if (!(o instanceof Void)) {
            response.setParam(o);
        }
    }

    private Object[] convertParam(Class<?>[] paramType, Request request) {
        Object[] params = new Object[paramType.length];
        for (int i = 0; i < params.length; i++) {
            Class<?> type = paramType[i];
            // todo 之后再单独抽出
            if (type.isAssignableFrom(SceneConnect.class)) {

            } else {
                params[i] = request.getParam();
            }
        }
        return params;
    }
}
