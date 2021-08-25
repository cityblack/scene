package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.lzh.game.scene.common.connect.Connect;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import static com.lzh.game.scene.common.ContextConstant.SOURCE_CONNECT_RELATION;

public abstract class AbstractSofaConnect implements Connect {

    protected AtomicLong CONNECT_COUNT = new AtomicLong();

    protected Connection connection;

    protected String address;

    protected String host;

    private int port;

    public AbstractSofaConnect(Connection connection, String address) {
        this.connection = connection;
        this.address = address;
        this.connection.setAttribute(SOURCE_CONNECT_RELATION, this);
        String[] values = address.split(":");
        this.host = values[0];
        this.port = Integer.parseInt(values[1]);
    }

    @Override
    public long reflectCount() {
        return CONNECT_COUNT.get();
    }

    @Override
    public <T> T getAttr(String key) {
        return (T) connection.getAttribute(key);
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
}
