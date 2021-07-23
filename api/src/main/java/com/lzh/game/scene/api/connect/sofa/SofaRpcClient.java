package com.lzh.game.scene.api.connect.sofa;

import com.alipay.remoting.rpc.RpcClient;

public class SofaRpcClient {

    private RpcClient rpcClient;

    private SofaRpcClient() {
        RpcClient client = new RpcClient();
        client.init();
        this.rpcClient = client;
    }

    private static SofaRpcClient client = new SofaRpcClient();

    public static SofaRpcClient getInstance() {
        return client;
    }

    public RpcClient client() {
        return rpcClient;
    }
}
