package com.lzh.game.scene.api.option;

import com.lzh.game.scene.api.server.SceneService;
import static com.lzh.game.scene.common.RequestSpace.*;
import com.lzh.game.scene.common.connect.server.cmd.Action;
import com.lzh.game.scene.common.connect.server.cmd.Cmd;
import com.lzh.game.scene.common.proto.SceneChangeListen;

/**
 * 监听服务主动推送
 */
@Action(LISTEN_INSTANCE_SPACE)
public class ListenController {

    private SceneService sceneService;

    public void setSceneService(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @Cmd(LISTEN_INSTANCE_CHANGE)
    public void onSceneChange(SceneChangeListen listen) {
        sceneService.onSceneChange(listen.getGroup(), listen.getInstance(), listen.getStatus());
    }
}
