package com.lzh.game.scene.core.service.impl.process;

import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.service.ReplicatorCmd;
import com.lzh.game.scene.core.service.SceneInstanceManage;

/**
 * 节点场景写入处理
 */
public class SceneInstanceProcess extends AbstractWriteProcess<SceneInstance> {

    private SceneInstanceManage manage;

    public SceneInstanceProcess(SceneInstanceManage manage) {
        this.manage = manage;
    }

    @Override
    public void doRequest(ReplicatorCmd cmd, SceneInstance data) {
        switch (cmd) {
            case REGISTER_SCENE: {
                manage.put(data.getGroup(), data);
                break;
            }
        }
    }

    @Override
    public Class<SceneInstance> getRequestParamType() {
        return SceneInstance.class;
    }
}
