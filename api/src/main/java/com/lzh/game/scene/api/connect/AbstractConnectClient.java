package com.lzh.game.scene.api.connect;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractConnectClient implements ConnectClient {

    private Map<String, Connect> connects = new ConcurrentHashMap<>();

    @Override
    public Connect getConnect(String key) {
        return connects.get(key);
    }

    @Override
    public Collection<Connect> getAllConnect() {
        return connects.values();
    }

    @Override
    public void putConnect(String key, Connect connect) {
        this.connects.put(key, connect);
    }

    @Override
    public boolean removeConnect(String key) {
        return Objects.nonNull(this.connects.remove(key));
    }

    protected Map<String, Connect> getConnects() {
        return connects;
    }
}
