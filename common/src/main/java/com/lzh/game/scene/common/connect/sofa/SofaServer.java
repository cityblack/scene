package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.connect.BootstrapConfig;
import com.lzh.game.scene.common.connect.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class SofaServer<T extends ServerConfig>
        extends AbstractServerBootstrap<T> implements ConnectServer<T> {

    private static final Logger logger = LoggerFactory.getLogger(SofaServer.class);

    public SofaServer(T config) {
        super(config);
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
        setRpcServer(this.getRpcServer());
        return server;
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
}
