package com.lzh.game.scene.api.controller;

import com.lzh.game.scene.api.connect.sofa.ApiClient;
import com.lzh.game.scene.api.scene.SceneService;
import static com.lzh.game.scene.common.RequestSpace.*;
import com.lzh.game.scene.common.connect.server.cmd.Action;
import com.lzh.game.scene.common.connect.server.cmd.Cmd;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.common.proto.SceneChangeListen;

/**
 * 监听服务主动推送
 */
@Action(LISTEN_INSTANCE_SPACE)
public class ListenController {

    private SceneService sceneService;

    private ApiClient clientServer;

    public void setSceneService(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    public void setClientServer(ApiClient clientServer) {
        this.clientServer = clientServer;
    }

    @Cmd(LISTEN_INSTANCE_CHANGE)
    public void onSceneChange(SceneChangeListen listen) {
        sceneService.onSceneChange(listen.getGroup(), listen.getInstance(), listen.getStatus());
    }

    @Cmd(LISTEN_NODE_CHANGE)
    public void onNodeChange(NodeInfo info) {
        clientServer.onNodeChange(info);
    }
}
