package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.exception.RemotingException;
import com.lzh.game.scene.common.connect.*;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SofaServerConnectFactory implements ConnectFactory {

    private static final Logger logger = LoggerFactory.getLogger(SofaServerConnectFactory.class);

    private AbstractServerBootstrap<?> bootstrap;

    public SofaServerConnectFactory(AbstractServerBootstrap<?> bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public Connect createConnect(String address, Object... param) {

        if (param[0] instanceof Connection) {
            return new SofaConnect((Connection) param[0], address, bootstrap.getIoExecutor());
        }
        throw new IllegalArgumentException("Create server connect error. param isn't Connect type");
    }

    private class SofaConnect extends AbstractSofaConnect {

        public SofaConnect(Connection connection, String address, Executor executor) {
            super(connection, address, executor);
        }

        @Override
        public void sendOneWay(Request request) {
            try {
                bootstrap.getRpcServer().oneway(connection, request);
            } catch (RemotingException e) {
                logger.error("Request error!!", e);
            }
        }

        @Override
        public <T>CompletableFuture<Response<T>> sendMessage(Request request) {
            CompletableFuture<Response<T>> future = new CompletableFuture<>();
            try {
                bootstrap.getRpcServer().invokeWithCallback(connection, request, newFutureCallBack(future), 5000);
            } catch (RemotingException e) {
                future.completeExceptionally(e);
            }
            return future;
        }
    }
}
