package com.lzh.game.scene.core.service;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import com.lzh.game.scene.common.connect.sofa.SofaConnectConnectedEvent;
import com.lzh.game.scene.core.ClusterServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 集群启动
 */
public abstract class SofaClusterServer<T extends ClusterServerConfig> extends AbstractServerBootstrap<T> {

    private static final Logger logger = LoggerFactory.getLogger(SofaClusterServer.class);

    public SofaClusterServer(T config) {
        setConfig(config);
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
        server.addConnectionEventProcessor(ConnectionEventType.CLOSE, new ConnectCloseEvent());
    }

    private class ConnectCloseEvent implements ConnectionEventProcessor {

        @Override
        public void onEvent(String remoteAddr, Connection conn) {
            String key = (String) conn.getAttribute(ContextConstant.SOURCE_CONNECT_RELATION);
            if (Objects.nonNull(key)) {
                getConnectManage().removeSceneConnect(key);
            } else {
                // 可能存在未分类的链接
                getConnectManage().removeConnect(remoteAddr);
            }
            logger.info("Close connect [{}-{}]!!", conn.getUrl(), key);
        }
    }

    public abstract RpcServer doInit(T config);
}
