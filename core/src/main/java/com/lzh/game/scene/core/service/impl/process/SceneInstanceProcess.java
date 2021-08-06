package com.lzh.game.scene.core.service.impl.process;

import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.service.ReplicatorCmd;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.service.impl.AbstractWriteProcess;

public class SceneInstanceProcess extends AbstractWriteProcess<SceneInstance> {

    private SceneInstanceManage manage;

    public SceneInstanceProcess(Class<SceneInstance> clazz, SceneInstanceManage manage) {
        super(clazz);
        this.manage = manage;
    }

    @Override
    public void onRequest(ReplicatorCmd cmd, SceneInstance data) {
        switch (cmd) {
            case REGISTER_SCENE: {
                manage.put(data.getGroup(), data);
                break;
            }
        }
    }
}
