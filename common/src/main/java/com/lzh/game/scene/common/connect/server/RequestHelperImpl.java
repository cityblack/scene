package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;

import java.util.Objects;

public class RequestHelperImpl implements RequestHelper {

    private Class<?>[] inner = new Class[]{Request.class, Response.class,
            Connect.class};

    @Override
    public Class<?>[] innerParam() {
        return inner;
    }

    @Override
    public Object[] paramConvert(Request request, MethodInvoke methodInvoke) {
        convertCheck(request, methodInvoke);

        Class<?>[] paramType = methodInvoke.params();
        Object[] params = new Object[paramType.length];

        for (int i = 0; i < params.length; i++) {
            Class<?> type = paramType[i];
            if (methodInvoke.extParamIndex() != i) {
                params[i] = convertInnerParam(type, request, request.getContext().getResponse(), request.getContext().getConnect());
            }
        }
        if (methodInvoke.extParamIndex() >= 0) {
            params[methodInvoke.extParamIndex()] = request.getParam();
        }
        return params;
    }

    private void convertCheck(Request request, MethodInvoke methodInvoke) {
        if (methodInvoke.extParamIndex() > -1) {
            Class<?> clazz = methodInvoke.params()[methodInvoke.extParamIndex()];
            if (clazz != request.getParamClass()) {
                throw new IllegalArgumentException("Proto error. mapping " + request.getId() + " Request param type isn't " + clazz.getName());
            }
        }
    }

    private Object convertInnerParam(Class<?> type, Object... params) {
        for (Object o : params) {
            if (Objects.isNull(o)) {
                continue;
            }
            if (o.getClass() == type) {
                return o;
            }
        }
        return null;
    }
}
