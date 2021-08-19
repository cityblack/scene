package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.sofa.SofaSceneConnect;

public class RequestHelperImpl implements RequestHelper {

    private Class<?>[] inner = new Class[]{ Request.class, Response.class
            , SofaSceneConnect.class, Connect.class, SceneConnect.class };

    @Override
    public Class<?>[] innerParam() {
        return inner;
    }

    @Override
    public Object[] paramConvert(Request request, MethodInvoke methodInvoke) {
        Class<?>[] paramType = methodInvoke.params();
        Object[] params = new Object[paramType.length];
        for (int i = 0; i < params.length; i++) {
            Class<?> type = paramType[i];
            if (methodInvoke.extParamIndex() != i) {
                params[i] = convert(type, request, request.getContext().getResponse(), request.getContext().getConnect());
            } else {
                if (methodInvoke.extParamIndex() >= 0) {
                    params[i] = request.getParam();
                }
            }
        }
        return params;
    }

    private Object convert(Class<?> type, Object...params) {
        for (Object o : params) {
            if (o.getClass() == type) {
                return o;
            }
        }
        return null;
    }
}
