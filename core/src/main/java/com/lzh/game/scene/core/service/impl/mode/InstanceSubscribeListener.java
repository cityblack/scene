package com.lzh.game.scene.core.service.impl.mode;

import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 如果批量上线?
 */
public class InstanceSubscribeListener {

    private static InstanceSubscribeListener listener = new InstanceSubscribeListener();

    private AbstractServerBootstrap<?> server;

    private Map<String, GroupListener> groupListeners;

    public static void init(AbstractServerBootstrap<?> server) {
        init(server, 2 << 8);
    }

    public static void init(AbstractServerBootstrap<?> server, int predictGroupSize) {
        listener.server = server;
        listener.groupListeners = new ConcurrentHashMap<>(predictGroupSize);
    }

    public static InstanceSubscribeListener getInstance() {
        return listener;
    }

    public void addListener(String group, String connectKey, int map, SceneChangeStatus status) {
        GroupListener listener = groupListeners.computeIfAbsent(group, e -> new GroupListener());
        listener.writeLock.lock();
        try {
            ClientListener client = listener.getClientListener(connectKey);
            if (map == ContextConstant.ALL_MAP_LISTEN_KEY) {
                client.allMap = true;
                client.allMapStatus = status;
            } else {
                client.mapListenStatus.put(map, status);
            }
        } finally {
            listener.writeLock.unlock();
        }
    }

    public void notifyListener(String group, SceneInstance instance, SceneChangeStatus status) {
        // group map
        GroupListener listener = groupListeners.computeIfAbsent(group, e -> new GroupListener());
        listener.readLock.lock();
        try {

        } finally {
            listener.readLock.unlock();
        }
    }

    protected class ClientListener {

        private Map<Integer, SceneChangeStatus> mapListenStatus = new HashMap<>();
        // 是否监控所有map的instance
        private boolean allMap;
        // 监听所有map的instance状态
        private SceneChangeStatus allMapStatus;
    }

    protected class GroupListener {

        private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = readWriteLock.readLock();
        private Lock writeLock = readWriteLock.writeLock();

        private Map<String, ClientListener> clientListeners = new HashMap<>();

        public ClientListener getClientListener(String connectKey) {
            return null;
        }

        public void notifyInstance(SceneInstance instance, SceneChangeStatus status) {

        }
    }

    private InstanceSubscribeListener() {
    }
}
