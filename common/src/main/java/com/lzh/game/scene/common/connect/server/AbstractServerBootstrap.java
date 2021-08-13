package com.lzh.game.scene.common.connect.server;

import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.common.connect.AbstractBootstrap;
import com.lzh.game.scene.common.connect.ConnectFactory;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;
import com.lzh.game.scene.common.connect.sofa.SofaServerConnectFactory;

import java.util.Objects;

public abstract class AbstractServerBootstrap<T extends ServerConfig>
        extends AbstractBootstrap implements ConnectServer<T> {

    private T config;

    private RpcServer rpcServer;

    @Override
    public T config() {
        return config;
    }

    @Override
    public int port() {
        return config.getPort();
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
    public InvokeManage invokeManage() {
        return getInvokeManage();
    }

    public RpcServer getRpcServer() {
        return rpcServer;
    }

    public void setRpcServer(RpcServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    public void setConfig(T config) {
        this.config = config;
    }

    protected abstract RpcServer init(T config);

    @Override
    protected ConnectFactory getDefaultFactory() {
        return new SofaServerConnectFactory(this);
    }

    @Override
    protected void doInit() {
        if (Objects.isNull(config)) {
            throw new IllegalArgumentException("Server config is null!!");
        }
        this.rpcServer = init(config());
    }

    @Override
    protected void doStart() {
        this.rpcServer.startup();
    }
}
