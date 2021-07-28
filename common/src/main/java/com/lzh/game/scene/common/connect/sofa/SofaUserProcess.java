package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.Connection;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcServer;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.lzh.game.scene.common.ContextDefined;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;
import com.lzh.game.scene.common.connect.server.RequestHandler;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class SofaUserProcess extends AsyncUserProcessor<Request> {

    static {
       SofaRpcSerializationRegister.registerCustomSerializer();
    }

    private RequestHandler requestHandler;

    private SceneConnectManage connectManage;

    private RpcServer rpcServer;

    public SofaUserProcess(RequestHandler requestHandler, SceneConnectManage connectManage) {
        this.requestHandler = requestHandler;
        this.connectManage = connectManage;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, Request request) {
        Connection connection = bizCtx.getConnection();
        Object key = connection.getAttribute(ContextDefined.SCENE_CONNECT_KEY);
        // 说明是已经注册过的链接 将其转成
        if (Objects.nonNull(key)) {
//            Connect connect = connectManage.getConnect((String) key);
//            requestHandler.dispatch(connect, request);
        } else {

        }
    }

    @Override
    public String interest() {
        return null;
    }

}
