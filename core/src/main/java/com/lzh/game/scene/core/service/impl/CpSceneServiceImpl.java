package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.jrfa.Replicator;
import com.lzh.game.scene.core.service.SceneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CpSceneServiceImpl implements SceneService {

    private static final Logger logger = LoggerFactory.getLogger(CpSceneServiceImpl.class);

    private SceneInstanceManage manage;

    private Replicator replicator;

    public CpSceneServiceImpl(SceneInstanceManage manage, Replicator replicator) {
        this.manage = manage;
        this.replicator = replicator;
    }

    @Override
    public void registerSceneInstance(Response response, String group, SceneInstance instance) {
        logger.info("Apply register scene instance. group:{} map:{}", group, instance.getMap());
        replicator
                .registerSceneInstance(instance)
                .exceptionally(throwable -> {
            response.setError(throwable.getMessage());
            return null;
        });
    }

    @Override
    public void removeSceneInstance(Response response, String group, SceneInstance instance) {

    }

    @Override
    public void getAllSceneInstances(Response response, String group) {

    }

    @Override
    public void getSceneInstances(Response response, String group, int map) {

    }

    @Override
    public void subscribe(Response response, String group, SceneChangeStatus status) {

    }

    @Override
    public void subscribe(Response response, String group, SceneChangeStatus status, int map) {

    }

    @Override
    public void keepMapInstances(Response response, String group, int map, int numLimit) {

    }
}
