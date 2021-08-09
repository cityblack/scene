package com.lzh.game.scene.core.jrfa.process;

import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.jrfa.ReplicatorCmd;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 节点场景写入处理
 */
public class SceneInstanceProcess extends AbstractWriteProcess<SceneInstance> {

    private static final Logger logger = LoggerFactory.getLogger(SceneInstanceProcess.class);

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
