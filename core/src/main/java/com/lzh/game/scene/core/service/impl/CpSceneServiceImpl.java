package com.lzh.game.scene.core.service.impl;

import com.google.common.eventbus.Subscribe;
import com.google.protobuf.ByteString;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.event.SceneInstanceEvent;
import com.lzh.game.scene.common.utils.EventBusUtils;
import com.lzh.game.scene.core.jrfa.ReplicatorCmd;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;
import com.lzh.game.scene.core.service.JRafClusterServer;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.service.SceneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CpSceneServiceImpl implements SceneService {

    private static final Logger logger = LoggerFactory.getLogger(CpSceneServiceImpl.class);

    private SceneInstanceManage manage;

    private JRafClusterServer connectServer;

    public CpSceneServiceImpl(SceneInstanceManage manage, JRafClusterServer connectServer) {
        this.manage = manage;
        this.connectServer = connectServer;
        EventBusUtils.getInstance().register(this);
    }

    @Override
    public void registerSceneInstance(String group, SceneInstance instance) {
        logger.info("Apply register scene instance. group:{} map:{}", group, instance.getMap());
        commitTask(ReplicatorCmd.REGISTER_SCENE, instance);
    }

    @Override
    public void removeSceneInstance(String group, String unique) {

    }

    @Override
    public List<SceneInstance> getSceneInstances(String group, int map) {
        if (map == ContextConstant.ALL_MAP_LISTEN_KEY) {
            return this.manage.get(group);
        }
        return this.manage.get(group, map);
    }

    @Override
    public void subscribe(Connect connect, String group, SceneChangeStatus status, int map) {

    }

    @Override
    public void keepMapInstances(String group, int map, int numLimit) {

    }

    @Override
    public void createScene(SceneConnect connect, String group, int mapId, int weight) {

    }

    private <T extends Serializable> CompletableFuture<Void> commitTask(ReplicatorCmd cmd, T param) {
        return connectServer.getJrService().commitWrite(param, buildRequest(ReplicatorCmd.REGISTER_SCENE, param));
    }

    private WriteRequest buildRequest(ReplicatorCmd cmd, Object param) {
        return WriteRequest.newBuilder()
                .setKey(cmd.getCmd())
                .setData(ByteString.copyFrom(connectServer.getJrService().serializer().encode(param)))
                .build();
    }

    @Subscribe
    public void onSceneChange(SceneInstanceEvent event) {

    }
}
