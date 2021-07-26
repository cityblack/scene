package com.lzh.game.scene.common.connect;

import com.alipay.remoting.rpc.RpcServer;

import java.util.Collection;

public class SofaServer implements ConnectServer {

    private ServerConfig config;

    private RpcServer rpcServer;

    private volatile boolean start = false;

    public SofaServer(ServerConfig config) {
        this.config = config;
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
    public ServerConfig config() {
        return config;
    }

    private void init(ServerConfig config) {
        RpcServer server = new RpcServer(config.getPort());
//        server.registerUserProcessor();

    }

    @Override
    public Connect getConnect(String key) {
        return null;
    }

    @Override
    public Collection<Connect> getAllConnect() {
        return null;
    }

    @Override
    public void putConnect(String key, Connect connect) {

    }

    @Override
    public boolean removeConnect(String key) {
        return false;
    }

    @Override
    public void shutdown() {

    }
}
