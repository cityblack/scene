package com.lzh.game.scene.api;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;

/**
 * ConnectionEventProcessor for ConnectionEventType.CONNECT
 *
 * @author xiaomin.cxm
 * @version $Id: CONNECTEventProcessor.java, v 0.1 Apr 8, 2016 10:58:48 AM xiaomin.cxm Exp $
 */
public class CONNECTEventProcessor implements ConnectionEventProcessor {

    private AtomicBoolean  connected    = new AtomicBoolean();
    private AtomicInteger  connectTimes = new AtomicInteger();
    private Connection     connection;
    private String         remoteAddr;
    private CountDownLatch latch        = new CountDownLatch(1);

    @Override
    public void onEvent(String remoteAddr, Connection conn) {
        doCheckConnection(conn);
        this.remoteAddr = remoteAddr;
        this.connection = conn;
        connected.set(true);
        connectTimes.incrementAndGet();
        latch.countDown();
    }

    /**
     * do check connection
     * @param conn
     */
    private void doCheckConnection(Connection conn) {
    }

    public boolean isConnected() throws InterruptedException {
        latch.await();
        return this.connected.get();
    }

    public int getConnectTimes() throws InterruptedException {
        latch.await();
        return this.connectTimes.get();
    }

    public Connection getConnection() throws InterruptedException {
        latch.await();
        return this.connection;
    }

    public String getRemoteAddr() throws InterruptedException {
        latch.await();
        return this.remoteAddr;
    }

    public void reset() {
        this.connectTimes.set(0);
        this.connected.set(false);
        this.connection = null;
    }
}
