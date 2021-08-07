package com.lzh.game.scene.core.jrfa.rpc;

import com.google.protobuf.ByteString;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.jrfa.JRService;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;
import com.lzh.game.scene.core.service.Replicator;
import com.lzh.game.scene.core.service.ReplicatorCmd;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public class ReplicatorImpl implements Replicator {

    private JRService jrService;

    public ReplicatorImpl(JRService jrService) {
        this.jrService = jrService;
    }

    @Override
    public CompletableFuture<Void> registerSceneInstance(SceneInstance sceneInstance) {
        return commitTask(ReplicatorCmd.REGISTER_SCENE, sceneInstance);
    }

    private WriteRequest buildRequest(ReplicatorCmd cmd, Object param) {
        return WriteRequest.newBuilder()
                .setKey(cmd.getCmd())
                .setData(ByteString.copyFrom(jrService.serializer().encode(param)))
                .build();
    }

    private <T extends Serializable> CompletableFuture<Void> commitTask(ReplicatorCmd cmd, T param) {

        return jrService.commitWrite(param, buildRequest(ReplicatorCmd.REGISTER_SCENE, param));
    }
}
