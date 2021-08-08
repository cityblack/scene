package com.lzh.game.scene.core.service.impl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.service.SceneInstanceManage;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// 应该都是要上锁 所以不拆分锁逻辑.
public class SceneInstanceManageImpl implements SceneInstanceManage {
    // group, map, instance
    private Table<String, Integer, List<SceneInstance>> groupMapKey = HashBasedTable.create();
    // group, unique, instance
    private Table<String, String, SceneInstance> groupUnique = HashBasedTable.create();
    // lock
    private Map<String, ReadWriteLock> lock = new ConcurrentHashMap<>();

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public List<SceneInstance> get(String group) {
        Lock lock = getLock(group).readLock();
        lock.lock();
        try {
            return null;
//            return new LinkedList<>(this.groupUnique.column(group).values());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<SceneInstance> get(String group, int mapId) {
        Lock lock = getLock(group).readLock();
        lock.lock();
        try {
            List<SceneInstance> list = this.groupMapKey.get(group, mapId);
            if (Objects.isNull(list) || list.isEmpty()) {
                return Collections.EMPTY_LIST;
            }
            return new LinkedList<>(list);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public SceneInstance getInstance(String group, String unique) {
        Lock lock = getLock(group).readLock();
        lock.lock();
        try {
            return this.groupUnique.get(group, unique);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean put(String group, SceneInstance instance) {
        Lock lock = getLock(group).writeLock();
        lock.lock();
        try {
            List<SceneInstance> list = groupMapKey.get(group, instance.getMap());
            if (Objects.isNull(list)) {
                list = new ArrayList<>();
                groupMapKey.put(group, instance.getMap(), list);
            }
            list.add(instance);

            groupUnique.put(group, instance.getUnique(), instance);
        } finally {
            lock.unlock();
        }
        return true;
    }

    private ReadWriteLock getLock(String group) {
//        return this.readWriteLock;
        return this.lock.computeIfAbsent(group, (k) -> new ReentrantReadWriteLock());
    }
}
