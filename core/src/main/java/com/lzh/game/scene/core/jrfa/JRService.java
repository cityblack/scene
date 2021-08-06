package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Node;
import com.google.protobuf.Message;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.ClusterServerConfig;
import com.lzh.game.scene.core.jrfa.rpc.entity.Response;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public interface JRService {

    <T extends ClusterServerConfig> void start(T config);

//    <E extends Message, T extends Message> CompletableFuture<E> commitTask(T request);

    /**
     * @param <P>     -- 返回的数据
     * @param data    -- 请求的原数据
     * @param request -- google Protobuf
     * @return
     */
    <T extends Serializable, R extends Message, P extends Response> CompletableFuture<P> commitTask(T data, R request);
//    void onRequest(DataRequest request);

//    void onResponse(DataResponse response);

    Serializer serializer();

    boolean isLeader();

    Node node();
}
