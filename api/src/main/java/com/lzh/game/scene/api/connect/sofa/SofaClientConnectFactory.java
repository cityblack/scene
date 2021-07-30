package com.lzh.game.scene.api.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.ConnectFactory;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.sofa.AbstractSofaConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class SofaClientConnectFactory implements ConnectFactory {

    private static final Logger logger = LoggerFactory.getLogger(SofaClientConnectFactory.class);

    private RpcClient rpcClient;

    public SofaClientConnectFactory(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public Connect createConnect(String address, Object param) {
        try {
            Connection connection = rpcClient.createStandaloneConnection(address, 5000);
            Connect connect = new SofaClientConnect(connection, address);
            return connect;
        } catch (RemotingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class SofaClientConnect extends AbstractSofaConnect {

        public SofaClientConnect(Connection connection, String address) {
            super(connection, address);
        }

        @Override
        public void sendOneWay(Request request) {
            try {
                rpcClient.oneway(connection, request);
            } catch (RemotingException e) {
                logger.error("Request error!!", e);
            }
        }

        @Override
        public CompletableFuture<Response> sendMessage(Request request) {
//            rpcClient.invokeWithFuture(connection, request, );
            return null;
        }
    }
}
