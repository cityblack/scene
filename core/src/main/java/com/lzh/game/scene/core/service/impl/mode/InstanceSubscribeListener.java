package com.lzh.game.scene.core.service.impl.mode;

import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import com.lzh.game.scene.common.proto.SceneChangeListen;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.lzh.game.scene.common.RequestSpace.*;

/**
 * 如果批量上线?
 */
public class InstanceSubscribeListener {

    private static final Logger logger = LoggerFactory.getLogger(InstanceSubscribeListener.class);

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
            listener.notifyInstance(instance, status);
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
            return this.clientListeners.computeIfAbsent(connectKey, e -> new ClientListener());
        }

        public void notifyInstance(SceneInstance instance, SceneChangeStatus status) {
            this.clientListeners.forEach((k, v) -> {
                if (v.allMap && isSendClientStatus(v.allMapStatus, status)) {
                    sendToClient(k, instance, status);
                } else {
                    Map<Integer, SceneChangeStatus> statusMap = v.mapListenStatus;
                    SceneChangeStatus cacheStatus = statusMap.get(instance.getMap());
                    if (Objects.nonNull(cacheStatus) && isSendClientStatus(v.allMapStatus, status)) {
                        sendToClient(k, instance, status);
                    }
                }
            });
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

            Request request = Request.of(cmd(LISTEN_INSTANCE_SPACE, LISTEN_INSTANCE_CHANGE));
            connect.sendOneWay(request);
        }

        private boolean isSendClientStatus(SceneChangeStatus status, SceneChangeStatus target) {
            return status == SceneChangeStatus.ALL || status == target;
        }
    }

    private InstanceSubscribeListener() {
    }
}
