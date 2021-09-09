package com.lzh.game.scene.api.scene.impl;

import com.lzh.game.scene.api.scene.SceneService;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static com.lzh.game.scene.common.ContextConstant.ALL_MAP_LISTEN_KEY;

public class SceneServiceImpl implements SceneService {

    private Map<String, Listen> listens = new ConcurrentHashMap<>();

    @Override
    public void onSceneChange(String group, SceneInstance instance, SceneChangeStatus status) {
        Listen listen = listens.get(group);
        if (Objects.nonNull(listen)) {
            listen.notifyObserve(instance, status);
        }
    }

    @Override
    public void addSceneChangeListen(String group, int mapId, Consumer<SceneInstance> consumer) {
        Listen listen = listens.computeIfAbsent(group, (k) -> new Listen());
        listen.addObserve(mapId, consumer);
    }

    class Listen {

        private Map<Integer, Consumer<SceneInstance>> observers = new ConcurrentHashMap<>();

        public void notifyObserve(SceneInstance instance, SceneChangeStatus status) {
            Consumer<SceneInstance> allMap = this.observers.get(ALL_MAP_LISTEN_KEY);
            if (Objects.nonNull(allMap)) {
                allMap.accept(instance);
                return;
            }
            Consumer<SceneInstance> single = this.observers.get(instance.getMap());
            if (Objects.nonNull(single)) {
                single.accept(instance);
            }
        }

        public void addObserve(int mapId, Consumer<SceneInstance> consumer) {
            observers.put(mapId, consumer);
        }
    }
}
