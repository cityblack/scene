package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.InvokeCallback;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

import static com.lzh.game.scene.common.ContextConstant.SOURCE_CONNECT_RELATION;

public abstract class AbstractSofaConnect implements Connect {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSofaConnect.class);

    protected AtomicLong CONNECT_COUNT = new AtomicLong();

    protected Connection connection;

    protected String address;

    protected String host;

    private int port;

    private Executor executor;

    public AbstractSofaConnect(Connection connection, String address, Executor executor) {
        this.connection = connection;
        this.address = address;
        this.connection.setAttribute(SOURCE_CONNECT_RELATION, this);
        String[] values = address.split(":");
        this.host = values[0];
        this.port = Integer.parseInt(values[1]);
        this.executor = executor;
    }

    @Override
    public long reflectCount() {
        return CONNECT_COUNT.get();
    }

    @Override
    public <T> T getAttr(String key) {
        Object o = connection.getAttribute(key);
        return Objects.isNull(o) ? null : (T)o;
    }

    @Override
    public void setAttr(String key, Object o) {
        connection.setAttributeIfAbsent(key, o);
    }

    @Override
    public String key() {
        return address;
    }

    @Override
    public String host() {
        return this.host;
    }

    @Override
    public int port() {
        return this.port;
    }

    @Override
    public void close() throws IOException {

    }

    protected <T>InvokeCallback newFutureCallBack(CompletableFuture<Response<T>> future) {
        return new InvokeCallback() {
            @Override
            public void onResponse(Object result) {
                try {
                    if (future.isCancelled()) {
                        return;
                    }
                    if (future.isDone()) {
                        logger.error("Response message but future is done!");
                        return;
                    }
                    future.complete((Response<T>) result);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onException(Throwable e) {
                if (future.isCancelled()) {
                    return;
                }
                future.completeExceptionally(e);
                logger.error("Response error:", e);
            }

            @Override
            public Executor getExecutor() {
                return executor;
            }
        };
    }
}
