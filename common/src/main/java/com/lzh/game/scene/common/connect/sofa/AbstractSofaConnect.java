package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.lzh.game.scene.common.connect.Connect;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractSofaConnect implements Connect {

    protected AtomicLong CONNECT_COUNT = new AtomicLong();

    protected Connection connection;

    protected String address;

    public AbstractSofaConnect(Connection connection, String address) {
        this.connection = connection;
        this.address = address;
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

}
