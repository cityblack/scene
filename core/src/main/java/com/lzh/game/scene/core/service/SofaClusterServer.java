package com.lzh.game.scene.core.service;

import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import com.lzh.game.scene.common.connect.sofa.SofaConnectCloseEvent;
import com.lzh.game.scene.common.connect.sofa.SofaConnectConnectedEvent;
import com.lzh.game.scene.core.ClusterServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 集群启动
 */
public abstract class SofaClusterServer<T extends ClusterServerConfig> extends AbstractServerBootstrap<T> {

    private static final Logger logger = LoggerFactory.getLogger(SofaClusterServer.class);

    public SofaClusterServer(T config) {
        super(config);
    }

    @Override
    public void shutdown() {

    }

    @Override
    protected RpcServer init(T config) {
        RpcServer rpcServer = doInit(config);
        this.rpcInit(rpcServer);
        return rpcServer;
    }

    private void rpcInit(RpcServer server) {
        server.registerUserProcessor(getSofaUserProcess());
        server.addConnectionEventProcessor(ConnectionEventType.CONNECT, new SofaConnectConnectedEvent(getConnectManage(), getConnectFactory()));
        server.addConnectionEventProcessor(ConnectionEventType.CLOSE, new SofaConnectCloseEvent(getConnectManage()));
    }

    public abstract RpcServer doInit(T config);
}
