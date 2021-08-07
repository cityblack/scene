package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcResponseClosure;
import com.lzh.game.scene.core.exception.FutureResponseException;
import com.lzh.game.scene.core.jrfa.rpc.entity.Response;

import java.util.concurrent.CompletableFuture;

public class FutureClosure<P, T extends Response> implements RpcResponseClosure<T> {

    private CompletableFuture<P> future;

    private T response;

    public FutureClosure(CompletableFuture<P> future) {
        this.future = future;
    }

    @Override
    public void run(Status status) {
        if (future.isCancelled()) {
            return;
        }
        if (!status.isOk()) {
            future.completeExceptionally(new FutureResponseException(status.getErrorMsg()));
        } else if (response.getSuccess()) {
            future.completeExceptionally(new FutureResponseException(response.getErrMsg()));
        } else {

//            byte[] data = response.getData();
//            future.complete();
        }
    }

    @Override
    public void setResponse(T resp) {
        this.response = resp;
    }
}
