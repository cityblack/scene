package com.lzh.game.scene.core.jrfa;

import com.alipay.remoting.rpc.RpcServer;
import com.alipay.sofa.jraft.Node;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.ClusterServerConfig;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;
import com.lzh.game.scene.core.service.Replicator;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public interface JRService {

    <T extends ClusterServerConfig> void start(T config);

    /**
     * @param data    -- 请求的原数据 主要用于减少主节点反序列化
     * @param request -- google Protobuf
     * @return
     */
    <T extends Serializable, R extends WriteRequest> CompletableFuture<Void> commitWrite(T data, R request);

    Serializer serializer();

    boolean isLeader();

    Node node();

    <R extends WriteRequest>void leaderWriteInvoke(R request, CompletableFuture<Void> future);

    Replicator replicator();

    RpcServer rpcServer();
}
