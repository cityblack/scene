package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.exception.RemotingException;
import com.lzh.game.scene.common.connect.*;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class SofaServerConnectFactory implements ConnectFactory {

    private static final Logger logger = LoggerFactory.getLogger(SofaServerConnectFactory.class);

    private AbstractServerBootstrap bootstrap;

    public SofaServerConnectFactory(AbstractServerBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public Connect createConnect(String address, Object param) {
        if (param instanceof Connection) {
            return new SofaConnect((Connection) param, address);
        }
        return null;
    }

    private class SofaConnect extends AbstractSofaConnect {

        public SofaConnect(Connection connection, String address) {
            super(connection, address);
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
        public CompletableFuture<Response> sendMessage(Request request) {
            throw new IllegalArgumentException("Server can't use future!!");
        }
    }
}
