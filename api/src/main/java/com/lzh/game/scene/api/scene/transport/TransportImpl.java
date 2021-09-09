package com.lzh.game.scene.api.scene.transport;

import com.lzh.game.scene.api.TransportSceneData;
import com.lzh.game.scene.api.scene.SceneLocalManage;
import com.lzh.game.scene.api.scene.SceneNodeBootstrap;
import com.lzh.game.scene.api.scene.Transport;
import com.lzh.game.scene.api.scene.TransportLocal;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.AbstractServerBootstrap;
import com.lzh.game.scene.common.connect.server.ConnectServer;
import com.lzh.game.scene.common.connect.server.ServerConfig;
import com.lzh.game.scene.common.utils.IpUtils;

import java.io.Serializable;
import java.util.Objects;

import static com.lzh.game.scene.api.scene.TransportLocal.NULL_INSTANCE;

public class TransportImpl implements Transport {

    private SceneLocalManage sceneLocalManage;

    private SceneNodeBootstrap bootstrap;

    private AbstractServerBootstrap<ServerConfig> serverBootstrap;

    public TransportImpl(SceneLocalManage sceneLocalManage, SceneNodeBootstrap bootstrap, AbstractServerBootstrap<ServerConfig> serverBootstrap) {
        this.sceneLocalManage = sceneLocalManage;
        this.bootstrap = bootstrap;
        this.serverBootstrap = serverBootstrap;
    }

    @Override
    public <K extends Serializable> void transportScene(String group, String sceneKey, TransportSceneData<K> request) {
        SceneInstance instance = sceneLocalManage.getSceneInstanceByKey(group, sceneKey);
        TransportLocal<K> transport = getTransport(request.getStrategy());
        if (Objects.isNull(instance)) {
            transport.onError(request, NULL_INSTANCE);
            return;
        }
        String address = instance.getAddress();
        SceneConnect connect = serverBootstrap.getConnectManage().getConnect(address);
        boolean local = IpUtils.isLocalIp(address) && connect.port() == serverBootstrap.getConfig().getPort();
        transport.transport(connect, request, local);
    }

    @Override
    public <K extends Serializable> void transportScene(String group, int map, TransportSceneData<K> request) {

    }

    protected TransportLocal getTransport(int strategy) {
        return bootstrap.getTransport(strategy);
    }

}
