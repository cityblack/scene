package com.lzh.game.scene.api.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.RpcClient;
import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.ClientLoadBalance;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.*;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;
import com.lzh.game.scene.common.connect.sofa.SofaConnectCloseEvent;
import com.lzh.game.scene.common.connect.sofa.SofaConnectConnectedEvent;
import com.lzh.game.scene.common.connect.sofa.SofaSceneConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * SOFA BLOT已经做好了各种网络机制，所以不再过度封装
 */
public class SofaConnectClient extends AbstractBootstrap<ApiConfig>
        implements ConnectClient {

    private final Logger logger = LoggerFactory.getLogger(SofaConnectClient.class);

    private RpcClient rpcClient;

    private LoadBalance loadBalance;

    public SofaConnectClient(ApiConfig config) {
        super(config);
    }

    private void init(ApiConfig config) {
        RpcClient client = new RpcClient();
        this.rpcClient = client;
        if (Objects.isNull(this.loadBalance)) {
            this.loadBalance = new ClientLoadBalance();
        }
        rpcClient.addConnectionEventProcessor(ConnectionEventType.CLOSE, new SofaConnectCloseEvent(getConnectManage()));
        rpcClient.registerUserProcessor(getSofaUserProcess());
    }

    private Connect create(String address) {
        Connect connect = connectManage().getConnect(address);
        if (Objects.isNull(connect)) {
            connect = getConnectFactory().createConnect(address);
            logger.info("Create connect client [{}]!!", address);
        }
        return connect;
    }

    @Override
    protected void doInit() {
        this.init(getConfig());
    }

    @Override
    protected void doStart() {
        if (this.rpcClient.isStarted()) {
            return;
        }
        this.rpcClient.startup();
        this.connectCluster(this.getConfig().getCluster());
    }

    @Override
    public void shutdown() {

    }

    @Override
    public SceneConnect createConnect(String address, NodeType type) {
        Connect connect = create(address);
        String key = SceneConnect.TO_UNIQUE.apply(address, type);
        SofaSceneConnect sceneConnect = new SofaSceneConnect(connect, type, key);
        return sceneConnect;
    }

    @Override
    public SceneConnect createConnect(String host, int port, NodeType type) {

        return createConnect(connectManage().toAddress(host, port), type);
    }

    @Override
    public SceneConnectManage sceneConnectManage() {
        return getConnectManage();
    }

    @Override
    public ConnectManage connectManage() {
        return getConnectManage();
    }

    @Override
    public SceneConnect getConnect(String address, NodeType type) {
        String key = SceneConnect.TO_UNIQUE.apply(address, type);
        SceneConnect connect = getConnectManage().getSceneConnect(key);
        if (Objects.nonNull(connect)) {
            return connect;
        }
        connect = createConnect(address, type);
        getConnectManage().putSceneConnect(key, connect);
        return connect;
    }

    @Override
    public SceneConnect getConnect(String host, int port, NodeType type) {
        return getConnect(connectManage().toAddress(host, port), type);
    }

    @Override
    public <T>CompletableFuture<Response<T>> sendMessage(Request request) {
        Connect connect = this.loadBalance.choose(getConnectManage().getAllConnect(), request);
        return connect.sendMessage(request);
    }

    @Override
    public void sendOneWay(Request request) {
        Connect connect = this.loadBalance.choose(getConnectManage().getAllConnect(), request);
        connect.sendOneWay(request);
    }

    @Override
    protected ConnectFactory getDefaultFactory() {
        return new SofaClientConnectFactory(this, getConfig().getRequestOutTime());
    }

    public RpcClient getRpcClient() {
        return rpcClient;
    }

    private void connectCluster(List<Member> members) {
        for (Member member : members) {
            getConnect(member.getHost(), member.getPort(), NodeType.SCENE_NODE);
        }
    }
}
