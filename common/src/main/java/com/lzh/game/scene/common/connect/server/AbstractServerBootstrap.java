package com.lzh.game.scene.common.connect.server;

import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.common.connect.AbstractBootstrap;
import com.lzh.game.scene.common.connect.BootstrapConfig;
import com.lzh.game.scene.common.connect.ConnectFactory;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;
import com.lzh.game.scene.common.connect.sofa.SofaServerConnectFactory;

import java.util.Objects;

public abstract class AbstractServerBootstrap<T extends ServerConfig>
        extends AbstractBootstrap<T> implements ConnectServer<T> {

    private RpcServer rpcServer;

    public AbstractServerBootstrap(T config) {
        super(config);
    }

    @Override
    public int port() {
        return getConfig().getPort();
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

    @Override
    protected ConnectFactory getDefaultFactory() {
        return new SofaServerConnectFactory(this);
    }

    @Override
    protected void doInit() {
        if (Objects.isNull(getConfig())) {
            throw new IllegalArgumentException("Server config is null!!");
        }
        this.rpcServer = init(getConfig());
    }

    @Override
    protected void doStart() {
        if (this.rpcServer.isStarted()) {
            return;
        }
        this.rpcServer.startup();
    }

    protected abstract RpcServer init(T config);

}
