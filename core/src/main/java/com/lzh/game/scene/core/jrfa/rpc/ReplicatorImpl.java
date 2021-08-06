package com.lzh.game.scene.core.jrfa.rpc;

import com.google.protobuf.ByteString;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.jrfa.JRService;
import com.lzh.game.scene.core.jrfa.rpc.entity.Response;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;
import com.lzh.game.scene.core.service.Replicator;
import com.lzh.game.scene.core.service.ReplicatorCmd;

import java.util.concurrent.CompletableFuture;

public class ReplicatorImpl implements Replicator {

    private JRService jrService;

    public ReplicatorImpl(JRService jrService) {
        this.jrService = jrService;
    }

    @Override
    public CompletableFuture<Response> registerSceneInstance(SceneInstance sceneInstance) {

        return jrService.commitTask(buildRequest(ReplicatorCmd.REGISTER_SCENE.getCmd(), sceneInstance), );
    }

    private WriteRequest buildRequest(int cmd, Object param) {
        return WriteRequest.newBuilder()
                .setKey(cmd)
                .setData(ByteString.copyFrom(jrService.serializer().encode(param)))
                .build();
    }
}
