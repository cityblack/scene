package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.server.convert.AbstractRequestParamConvert;
import com.lzh.game.scene.common.connect.server.convert.RequestParamConvert;

import java.util.Objects;

public class RequestHelperImpl implements RequestHelper {

    @Override
    public Object[] paramConvert(Request request, MethodInvoke methodInvoke) {
        convertCheck(request, methodInvoke);

        Class<?>[] paramType = methodInvoke.params();
        Object[] params = new Object[paramType.length];

        for (int i = 0; i < params.length; i++) {
            Class<?> type = paramType[i];
            if (methodInvoke.extParamIndex() != i) {
                params[i] = convertInnerParam(type, request);
            }
        }
        if (methodInvoke.extParamIndex() >= 0) {
            params[methodInvoke.extParamIndex()] = request.getParam();
        }
        return params;
    }

    private void convertCheck(Request request, MethodInvoke methodInvoke) {
        /*if (methodInvoke.extParamIndex() > -1) {
            Class<?> clazz = methodInvoke.params()[methodInvoke.extParamIndex()];
            if (clazz != request.getParamClass()) {
                throw new IllegalArgumentException("Proto error. mapping " + request.getId() + " Request param type isn't " + clazz.getName());
            }
        }*/
    }

    private Object convertInnerParam(Class<?> type, Request request) {
        RequestParamConvert convert = AbstractRequestParamConvert.getConvert(type);
        return Objects.isNull(convert) ? null : convert.convert(request);
    }
}
