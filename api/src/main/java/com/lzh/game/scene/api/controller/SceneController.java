package com.lzh.game.scene.api.controller;

import com.lzh.game.scene.api.scene.SceneNodeBootstrap;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.server.cmd.Action;
import com.lzh.game.scene.common.connect.server.cmd.Cmd;

import static com.lzh.game.scene.common.RequestSpace.*;

@Action(NODE_SPACE)
public class SceneController {

    private SceneNodeBootstrap bootstrap;

    public void setBootstrap(SceneNodeBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    /**
     * 远程端发起创建场景
     * @param map
     * @return
     */
    @Cmd(NODE_CLIENT_CREATE)
    public SceneInstance createScene(int map) {
        SceneInstance instance = bootstrap.getProcess().createScene(map);
        return instance;
    }
}
