package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;

public class RequestDispatchHandler implements RequestHandler {

    private CmdManage cmdManage;

    @Override
    public void dispatch(Request request) {
        Invoke invoke = cmdManage.getInvoke(request.getId());
        Response response = new Response();
        try {
            Object value = invoke.invoke(request.getParam());
            response.setParam(value);
        } catch (InvokeException e) {
            // 一般这种报错在开发期都会避免
            response.setError(e.getMessage());
        }
    }
}
