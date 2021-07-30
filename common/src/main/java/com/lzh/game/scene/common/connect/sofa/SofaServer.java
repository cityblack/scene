package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.common.ContextDefined;
import com.lzh.game.scene.common.connect.AbstractBootstrap;
import com.lzh.game.scene.common.connect.ConnectFactory;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;
import com.lzh.game.scene.common.connect.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class SofaServer extends AbstractBootstrap implements ConnectServer {

    private static final Logger logger = LoggerFactory.getLogger(SofaServer.class);

    private ServerConfig config;

    private RpcServer rpcServer;

    private volatile boolean start = false;

    public SofaServer(ServerConfig config) {
        this.config = config;
    }

    @Override
    public int port() {
        return config.getPort();
    }

    @Override
    public void start() {
        if (start) {
            return;
        }
        init(this.config);
        rpcServer.start();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public ServerConfig config() {
        return config;
    }

    @Override
    public SceneConnectManage manage() {
        return getConnectManage();
    }

    @Override
    public RequestHandler requestHandler() {
        return getRequestHandler();
    }

    @Override
    public CmdClassManage classManage() {
        return getClassManage();
    }

    @Override
    public InvokeManage invokeManage() {
        return getInvokeManage();
    }

    private void init(ServerConfig config) {
        this.build();
        RpcServer server = new RpcServer(config.getPort());
        server.registerUserProcessor(getSofaUserProcess());
        server.addConnectionEventProcessor(ConnectionEventType.CONNECT, new SofaConnectConnectedEvent(getConnectManage(), getConnectFactory()));
        server.addConnectionEventProcessor(ConnectionEventType.CLOSE, new ConnectCloseEvent());
        this.rpcServer = server;
    }

    @Override
    protected ConnectFactory getDefaultFactory() {
        return new SofaServerConnectFactory();
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
}
