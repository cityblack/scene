package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import com.lzh.game.scene.common.connect.server.ConnectServer;
import com.lzh.game.scene.common.connect.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SofaServer<T extends ServerConfig>
        extends AbstractServerBootstrap<T> implements ConnectServer<T> {

    private static final Logger logger = LoggerFactory.getLogger(SofaServer.class);

    public SofaServer(T config) {
        super(config);
    }

    @Override
    protected RpcServer init(T config) {
        RpcServer server = new RpcServer(config.getPort());
        server.registerUserProcessor(getSofaUserProcess());
        server.addConnectionEventProcessor(ConnectionEventType.CONNECT, new SofaServerConnectedEvent(getConnectFactory()));
        server.addConnectionEventProcessor(ConnectionEventType.CLOSE, new SofaConnectCloseEvent(getConnectManage()));
        setRpcServer(this.getRpcServer());
        return server;
    }
}
