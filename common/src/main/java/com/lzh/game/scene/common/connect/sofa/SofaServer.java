package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.common.connect.server.ConnectServer;
import com.lzh.game.scene.common.connect.server.ServerConfig;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;

public class SofaServer implements ConnectServer {

    private ServerConfig config;

    private RpcServer rpcServer;

    private SofaUserProcess sofaUserProcess;

    private SofaConnectConnectedEvent createEvent = new SofaConnectConnectedEvent();

    private SceneConnectManage<SceneConnect> connectManage;

    private volatile boolean start = false;

    public SofaServer(ServerConfig config) {
        this.config = config;
        this.init(this.config);
    }

    @Override
    public int port() {
        return 0;
    }

    @Override
    public void start() {
        if (start) {
            return;
        }
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
    public SceneConnectManage<SceneConnect> manage() {
        return null;
    }

    private void init(ServerConfig config) {
        RpcServer server = new RpcServer(config.getPort());
        server.registerUserProcessor(sofaUserProcess);
        server.addConnectionEventProcessor(ConnectionEventType.CONNECT, createEvent);

        this.rpcServer = server;
    }
}
