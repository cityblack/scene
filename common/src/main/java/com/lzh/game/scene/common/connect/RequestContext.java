package com.lzh.game.scene.common.connect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestContext {

    // 使用并发容器是因为调用的时候可能不在一个线程中
    private Map<String, Object> params = new ConcurrentHashMap<>();

    public void setAttr(String key, Object value) {
        this.params.put(key, value);
    }

    public <T>T getAttr(String key) {
        return (T) params.get(key);
    }
}
