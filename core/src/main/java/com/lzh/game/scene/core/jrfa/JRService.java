package com.lzh.game.scene.core.jrfa;

import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.ClusterServerConfig;
import com.lzh.game.scene.core.exchange.DataRequest;
import com.lzh.game.scene.core.exchange.DataResponse;

import java.util.concurrent.CompletableFuture;

public interface JRService {

    <T extends ClusterServerConfig>void start(T config);

    CompletableFuture<Response> commitTask(DataRequest request);

    void onRequest(DataRequest request);

    void onResponse(DataResponse response);

    Serializer serializer();
}
