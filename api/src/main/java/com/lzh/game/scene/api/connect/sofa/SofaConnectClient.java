package com.lzh.game.scene.api.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.AbstractConnectManage;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SOFA BLOT已经做好了各种网络机制，所以不再过度封装
 */
public class SofaConnectClient extends AbstractConnectManage<Connect> implements ConnectClient<Connect> {

    private final Logger logger = LoggerFactory.getLogger(SofaConnectClient.class);

    private RpcClient rpcClient = SofaRpcClient.getInstance().client();

    private ConnectionEventProcessor closeEvent = new ConnectCloseEvent(this);

    private static final String ID_SIGN = "connect_id";

    public SofaConnectClient() {

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
            return wrapper(connection);
        } catch (RemotingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Connect createConnect(String host, int port) {
        try {
            Connection connection = rpcClient.createStandaloneConnection(host, port, 5000);
            return wrapper(connection);
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

    private SofaConnect wrapper(Connection connection) {
        rpcClient.addConnectionEventProcessor(ConnectionEventType.CLOSE, closeEvent);
        SofaConnect connect = new SofaConnect(connection);
        return connect;
    }

    private class SofaConnect implements SceneConnect {

        private AtomicLong CONNECT_COUNT = new AtomicLong();

        private Connection connection;

        public SofaConnect(Connection connection) {
            this.connection = connection;
        }

        @Override
        public Response sendMessage(Request request) {
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
        public NodeType type() {
            return null;
        }

        @Override
        public String key() {
            return null;
        }
    }

    private class ConnectCloseEvent implements ConnectionEventProcessor {

        private SofaConnectClient client;

        public ConnectCloseEvent(SofaConnectClient client) {
            this.client = client;
        }

        @Override
        public void onEvent(String remoteAddr, Connection conn) {
            String key = (String) conn.getAttribute(ID_SIGN);
            client.removeConnect(key);
            logger.info("Close connect [{}-{}]!!", conn.getUrl(), key);
        }
    }
}
