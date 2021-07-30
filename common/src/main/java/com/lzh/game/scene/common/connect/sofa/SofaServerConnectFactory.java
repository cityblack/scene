package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.common.connect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class SofaServerConnectFactory implements ConnectFactory {

    private static final Logger logger = LoggerFactory.getLogger(SofaServerConnectFactory.class);

    private RpcServer rpcServer;

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
                rpcServer.oneway(connection, request);
            } catch (RemotingException e) {
                logger.error("Request error!!", e);
            }
        }

        @Override
        public CompletableFuture<Response> sendMessage(Request request) {
            return null;
        }
    }
}
