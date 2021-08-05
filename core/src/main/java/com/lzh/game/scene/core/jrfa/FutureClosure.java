package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcResponseClosure;
import com.google.protobuf.Message;
import com.lzh.game.scene.core.exception.FutureResponseException;

import java.util.concurrent.CompletableFuture;

public class FutureClosure<T extends Message> implements RpcResponseClosure<T> {

    private CompletableFuture<T> future;

    public FutureClosure(CompletableFuture<T> future) {
        this.future = future;
    }

    @Override
    public void run(Status status) {
        if (future.isCancelled()) {
            return;
        }
        if (!status.isOk()) {
            future.completeExceptionally(new FutureResponseException(status.getErrorMsg()));
        }
    }

    @Override
    public void setResponse(T resp) {
        if (future.isCancelled()) {
            return;
        }
        if (!future.isDone()) {
            future.complete(resp);
        }
    }
}
