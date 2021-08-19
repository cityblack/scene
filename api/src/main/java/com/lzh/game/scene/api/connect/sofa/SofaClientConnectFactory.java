package com.lzh.game.scene.api.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.InvokeCallback;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.ConnectFactory;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import com.lzh.game.scene.common.connect.sofa.AbstractSofaConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.lzh.game.scene.common.ContextConstant.SOURCE_CONNECT_RELATION;

public class SofaClientConnectFactory implements ConnectFactory {

    private static final Logger logger = LoggerFactory.getLogger(SofaClientConnectFactory.class);

    private SofaConnectClient client;

    private int requestOutTime;

    public SofaClientConnectFactory(SofaConnectClient client, int requestOutTime) {
        this.client = client;
        this.requestOutTime = requestOutTime;
    }

    @Override
    public Connect createConnect(String address, Object... param) {
        try {
            Connection connection = client.getRpcClient().createStandaloneConnection(address, 5000);
            Connect connect = new SofaClientConnect(connection, address);
            return connect;
        } catch (RemotingException e) {
            logger.error("Create connect error:", e);
            throw new RuntimeException("Create connect error:", e);
        }
    }

    private class SofaClientConnect extends AbstractSofaConnect {

        public SofaClientConnect(Connection connection, String address) {
            super(connection, address);
        }

        @Override
        public void sendOneWay(Request request) {
            try {
                client.getRpcClient().oneway(connection, request);
            } catch (RemotingException e) {
                logger.error("Request error!!", e);
            }
        }

        @Override
        public <T>CompletableFuture<Response<T>> sendMessage(Request request) {
            CompletableFuture<Response<T>> future = new CompletableFuture<>();
            try {
                client.getRpcClient().invokeWithCallback(connection, request, new InvokeCallback() {

                    @Override
                    public void onResponse(Object result) {
                        try {
                            if (future.isCancelled()) {
                                return;
                            }
                            if (future.isDone()) {
                                logger.error("Response message but future is done!");
                                return;
                            }
                            future.complete((Response) result);
                        } catch (Exception e) {
                            future.completeExceptionally(e);
                        }
                    }

                    @Override
                    public void onException(Throwable e) {
                        if (future.isCancelled()) {
                            return;
                        }
                        future.completeExceptionally(e);
                        logger.error("Response error:", e);
                    }

                    @Override
                    public Executor getExecutor() {
                        return null;
                    }
                }, requestOutTime);
            } catch (RemotingException e) {
                future.completeExceptionally(e);
            }
            return future;
        }
    }
}
