package com.lzh.game.scene.core.service.impl.mode;

import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import com.lzh.game.scene.common.proto.SceneChangeListen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.lzh.game.scene.common.RequestSpace.*;

/**
 * 如果批量上线?
 * 是否直接通过group绑定线程 不上锁和并发容器？
 */
public class InstanceSubscribeListener implements InstanceSubscribe {

    private static final Logger logger = LoggerFactory.getLogger(InstanceSubscribeListener.class);

    private AbstractServerBootstrap<?> server;

    private Map<String, GroupListener> groupListeners;

    private Map<String, ClientListener> clients;

    public InstanceSubscribeListener(AbstractServerBootstrap<?> server, int predictGroupSize) {
        this.server = server;
        // 并发容器 减少锁的力度
        this.groupListeners = new ConcurrentHashMap<>(predictGroupSize);
        this.clients = new ConcurrentHashMap<>();
    }

    public InstanceSubscribeListener(AbstractServerBootstrap<?> server) {
        this(server, 2 << 8);
    }

    @Override
    public void addListener(String group, String connectKey, int map, SceneChangeStatus status) {
        GroupListener listener = groupListeners.computeIfAbsent(group, e -> new GroupListener());
        listener.writeLock.lock();
        try {
            listener.clientListeners.add(connectKey);
            ClientListener client = clients.computeIfAbsent(connectKey, e -> new ClientListener());
            if (map == ContextConstant.ALL_MAP_LISTEN_KEY) {
                client.allMap = true;
                client.allMapStatus = status;
            } else {
                client.mapListenStatus.put(map, status);
            }
            client.addGroupKey(group);
        } finally {
            listener.writeLock.unlock();
        }
    }

    @Override
    public void notifyListener(String group, SceneInstance instance, SceneChangeStatus status) {
        // group map
        GroupListener listener = groupListeners.get(group);
        if (Objects.isNull(listener)) {
            return;
        }
        listener.readLock.lock();
        try {
            listener.clientListeners.forEach(k -> {
                ClientListener client = clients.get(k);
                if (Objects.isNull(client)) {
                    return;
                }
                if (!client.isSendClientStatus(status)) {
                    return;
                }
                if (client.allMap) {
                    client.sendToClient(k, instance, status);
                } else {
                    Map<Integer, SceneChangeStatus> statusMap = client.mapListenStatus;
                    SceneChangeStatus cacheStatus = statusMap.get(instance.getMap());
                    if (Objects.nonNull(cacheStatus) && client.isSendClientStatus(status)) {
                        client.sendToClient(k, instance, status);
                    }
                }
            });
        } finally {
            listener.readLock.unlock();
        }
    }

    @Override
    public void removeListener(String connectKey) {
        ClientListener listener = this.clients.remove(connectKey);
        if (Objects.isNull(listener)) {
            return;
        }
        Set<String> groups = listener.groupsKey;
        for (String group : groups) {
            GroupListener groupListener = this.groupListeners.get(group);
            if (Objects.isNull(groupListener)) {
                continue;
            }
            groupListener.clientListeners.remove(connectKey);
        }
    }

    protected class ClientListener {

        private Map<Integer, SceneChangeStatus> mapListenStatus = new HashMap<>();
        // 是否监控所有map的instance
        private boolean allMap;
        // 监听所有map的instance状态
        private SceneChangeStatus allMapStatus;
        // 删除引用 指向哪些组包含该对象
        private Set<String> groupsKey = new HashSet<>();

        private boolean isSendClientStatus(SceneChangeStatus status) {
            return allMapStatus == SceneChangeStatus.ALL || status == allMapStatus;
        }

        private void sendToClient(String key, SceneInstance instance, SceneChangeStatus status) {
            SceneConnect connect = server.getConnectManage().getConnect(key);
            if (Objects.isNull(connect)) {
                logger.error("Change instance push to client error, cause of can't find connect with key:{} instance:{} status:{}"
                        , key, instance, status.name());
                return;
            }
            SceneChangeListen listen = new SceneChangeListen();
            listen.setGroup(instance.getGroup());
            listen.setInstance(instance);
            listen.setStatus(status);

            Request request = Request.of(cmd(LISTEN_INSTANCE_SPACE, LISTEN_INSTANCE_CHANGE), listen);
            connect.sendOneWay(request);
        }

        public void addGroupKey(String group) {
            this.groupsKey.add(group);
        }
    }

    protected class GroupListener {

        private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = readWriteLock.readLock();
        private Lock writeLock = readWriteLock.writeLock();

        private Set<String> clientListeners = new HashSet<>();
    }

    private InstanceSubscribeListener() {
    }
}
