package com.lzh.game.scene.api.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.common.connect.*;
import com.lzh.game.scene.common.connect.sofa.SofaUserProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SOFA BLOT已经做好了各种网络机制，所以不再过度封装
 */
public class SofaConnectClient extends AbstractConnectManage<Connect> implements ConnectClient<Connect> {

    private final Logger logger = LoggerFactory.getLogger(SofaConnectClient.class);

    private RpcClient rpcClient = SofaRpcClient.getInstance().client();

    private ConnectionEventProcessor closeEvent = new ConnectCloseEvent(this);

    public SofaConnectClient() {
        rpcClient.addConnectionEventProcessor(ConnectionEventType.CLOSE, closeEvent);
        rpcClient.registerUserProcessor(new SofaUserProcess());
    }

    @Override
    public void init(ApiConfig config) {
        List<Member> members = config.getCluster();
        for (Member member : members) {
            String address = toAddress(member.getHost(), member.getPort());
            Connect connect = createConnect(address);
            if (Objects.nonNull(connect)) {
                putConnect(address, connect);
            }
        }
    }

    @Override
    public Connect createConnect(String address) {
        try {
            Connection connection = rpcClient.createStandaloneConnection(address, 5000);
            return wrapper(connection, address);
        } catch (RemotingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Connect createConnect(String host, int port) {
        try {
            Connection connection = rpcClient.createStandaloneConnection(host, port, 5000);
            return wrapper(connection, toAddress(host, port));
        } catch (RemotingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onConnectClose(Connect connect) {

    }

    @Override
    public void shutdown() {
        rpcClient.shutdown();
    }

    private SofaConnect wrapper(Connection connection, String address) {
        SofaConnect connect = new SofaConnect(connection, address);
        connect.setAttr(Connect.KEY_SIGN, connect.address());
        logger.info("Create connect client [{}]!!", address);
        return connect;
    }

    private class SofaConnect implements Connect {

        private AtomicLong CONNECT_COUNT = new AtomicLong();

        private Connection connection;

        private String address;

        public SofaConnect(Connection connection, String address) {
            this.connection = connection;
            this.address = address;
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

        @Override
        public long reflectCount() {
            return CONNECT_COUNT.get();
        }

        @Override
        public Object getAttr(String key) {
            return connection.getAttribute(key);
        }

        @Override
        public void setAttr(String key, Object o) {
            connection.setAttributeIfAbsent(key, o);
        }

        @Override
        public String address() {
            return address;
        }
    }

    private class ConnectCloseEvent implements ConnectionEventProcessor {

        private SofaConnectClient client;

        public ConnectCloseEvent(SofaConnectClient client) {
            this.client = client;
        }

        @Override
        public void onEvent(String remoteAddr, Connection conn) {
            String key = (String) conn.getAttribute(Connect.KEY_SIGN);
            client.removeConnect(key);
            logger.info("Close connect [{}-{}]!!", conn.getUrl(), key);
        }
    }
}
