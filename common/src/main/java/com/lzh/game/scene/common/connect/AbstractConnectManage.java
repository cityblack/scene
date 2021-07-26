package com.lzh.game.scene.common.connect;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractConnectManage<T extends Connect>
        implements ConnectManage<T> {

    private Map<String, T> connects = new ConcurrentHashMap<>();

    @Override
    public void putConnect(String key, T connect) {
        this.connects.put(key, connect);
    }

    @Override
    public T getConnect(String key) {
        return this.connects.get(key);
    }

    @Override
    public boolean removeConnect(String key) {
        return Objects.nonNull(this.connects.remove(key));
    }

    @Override
    public Collection<T> getAllConnect() {
        return this.connects.values();
    }

    /*private Map<NodeType, List<T>> typeConnects = new ConcurrentHashMap<>();

    private Map<String, T> scenesConnects = new ConcurrentHashMap<>();

    @Override
    public void putConnect(String key, T connect) {
        this.connects.put(key, connect);
        NodeType type = connect.type();
        String unique =  type.getName() + key;
        this.scenePut(unique, connect);
    }

    @Override
    public boolean removeConnect(String key) {
        T connect = connects.remove(key);
        if (Objects.isNull(connect)) {
            return false;
        }
        return sceneRemove(connect.key());
    }

    @Override
    public void scenePut(String key, T connect) {
        // 使用容器的锁
        this.scenesConnects.put(key, connect);
        this.typeConnects.merge(connect.type(), Arrays.asList(connect), (o1, o2) -> {
            o1.addAll(o2);
            return o1;
        });
    }

    @Override
    public boolean sceneRemove(String key) {
        T connect = this.scenesConnects.remove(key);
        if (Objects.isNull(connect)) {
            return false;
        }
        NodeType type = connect.type();
        List<T> connects = this.typeConnects.getOrDefault(type, new ArrayList<>());
        for (Iterator<T> iterator = connects.iterator(); iterator.hasNext(); ) {
            T t = iterator.next();
            if (t.key().equals(key)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }*/
}
