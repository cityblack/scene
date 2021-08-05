package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Node;
import com.google.protobuf.Message;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.ClusterServerConfig;

import java.util.concurrent.CompletableFuture;

public interface JRService {

    <T extends ClusterServerConfig> void start(T config);

    <E extends Message, T extends Message> CompletableFuture<E> commitTask(T request);

//    void onRequest(DataRequest request);

//    void onResponse(DataResponse response);

    Serializer serializer();

    <T extends Message, R extends Message> void applyOperation(Node node, T data, final CompletableFuture<R> future);

    boolean isLeader();

    Node node();
}
