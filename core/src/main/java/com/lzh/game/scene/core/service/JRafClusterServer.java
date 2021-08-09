package com.lzh.game.scene.core.service;

import com.alipay.remoting.rpc.RpcServer;
import com.lzh.game.scene.core.ClusterServerConfig;
import com.lzh.game.scene.core.jrfa.JRService;
import com.lzh.game.scene.core.jrfa.JRServiceImpl;

public class JRafClusterServer<T extends ClusterServerConfig> extends SofaClusterServer<T> {

    private JRService jrService;

    public JRafClusterServer(T config) {
        super(config);
    }

    public JRService getJrService() {
        return jrService;
    }

    public void setJrService(JRService jrService) {
        this.jrService = jrService;
    }

    @Override
    public RpcServer doInit(T config) {
        return createJRServer(config);
    }

    private RpcServer createJRServer(T config) {
        JRService service = new JRServiceImpl(getSerializer());
        service.start(config);
        this.jrService = service;
        return service.rpcServer();
    }
}
