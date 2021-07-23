package com.lzh.game.scene.api.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.InvokeCallback;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.lzh.game.scene.api.client.Request;
import com.lzh.game.scene.api.client.Response;
import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.AbstractConnectClient;
import com.lzh.game.scene.api.connect.Connect;
import com.lzh.game.scene.common.NodeType;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SOFA BLOT已经做好了各种网络机制，所以不再过度封装
 */
public class SofaConnectClient extends AbstractConnectClient {

    private RpcClient rpcClient = SofaRpcClient.getInstance().client();

    private AtomicInteger CONNECT_COUNT = new AtomicInteger();

    public SofaConnectClient() {

    }

    @Override
    public void init(ApiConfig config) {
        List<Member> members = config.getCluster();
        for (Member member : members) {
            String address = member.getHost() + ":" + member.getPort();
            Connect connect = createConnect(address, NodeType.SCENE_MANAGE_NODE);
            if (Objects.nonNull(connect)) {
                putConnect(connect.key(), connect);
            }
        }
    }

    @Override
    public Connect createConnect(String address, NodeType type) {
        try {
            Connection connection = rpcClient.getConnection(address, 5000);
            rpcClient.enableConnHeartbeat(address);
            String key = type.getName() + CONNECT_COUNT.incrementAndGet();
            SofaConnect connect = new SofaConnect(key, connection, type);
            return connect;
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void shutdown() {
        rpcClient.shutdown();
        getConnects().clear();
    }

    private class SofaConnect implements Connect {

        private String key;

        private Connection connection;

        private NodeType type;

        public SofaConnect(String key, Connection connection, NodeType type) {
            this.key = key;
            this.connection = connection;
            this.type = type;
        }

        @Override
        public Response sendMessage(Request request) {
//            rpcClient.invokeWithCallback();
            // context
            return null;
        }

        @Override
        public String key() {
            return null;
        }

        @Override
        public Member member() {
            return null;
        }

        @Override
        public int ReflectCount() {
            return 0;
        }

        @Override
        public NodeType nodeType() {
            return null;
        }
    }


}
