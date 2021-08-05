package com.lzh.game.scene.core.service;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.common.ContextDefined;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import com.lzh.game.scene.common.connect.sofa.SofaConnectConnectedEvent;
import com.lzh.game.scene.core.ClusterServerConfig;
import com.lzh.game.scene.core.jrfa.JRService;
import com.lzh.game.scene.core.jrfa.JRServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 集群启动
 */
public class SofaClusterServer<T extends ClusterServerConfig> extends AbstractServerBootstrap<T> {

    private static final Logger logger = LoggerFactory.getLogger(SofaClusterServer.class);

    private JRService jrService;

    public SofaClusterServer(T config) {
        setConfig(config);
    }

    @Override
    public void shutdown() {

    }

    @Override
    protected RpcServer init(T config) {
        RpcServer server = new RpcServer(config.getPort());
        server.registerUserProcessor(getSofaUserProcess());
        server.addConnectionEventProcessor(ConnectionEventType.CONNECT, new SofaConnectConnectedEvent(getConnectManage(), getConnectFactory()));
        server.addConnectionEventProcessor(ConnectionEventType.CLOSE, new ConnectCloseEvent());
        JRService service = createJRServer(config, server);
        this.jrService = service;
        return server;
    }

    private JRService createJRServer(T config, RpcServer server) {
        JRService service = new JRServiceImpl(server, getSerializer());
        service.start(config);
        return service;
    }

    private class ConnectCloseEvent implements ConnectionEventProcessor {

        @Override
        public void onEvent(String remoteAddr, Connection conn) {
            String key = (String) conn.getAttribute(ContextDefined.SOURCE_CONNECT_RELATION);
            if (Objects.nonNull(key)) {
                getConnectManage().removeSceneConnect(key);
            } else {
                // 可能存在未分类的链接
                getConnectManage().removeConnect(remoteAddr);
            }
            logger.info("Close connect [{}-{}]!!", conn.getUrl(), key);
        }
    }

    public JRService getJrService() {
        return jrService;
    }

    public void setJrService(JRService jrService) {
        this.jrService = jrService;
    }
}
