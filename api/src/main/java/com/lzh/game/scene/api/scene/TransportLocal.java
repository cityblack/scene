package com.lzh.game.scene.api.scene;

import com.lzh.game.scene.api.TransportSceneData;
import com.lzh.game.scene.common.connect.scene.SceneConnect;

import java.io.Serializable;

public interface TransportLocal<K extends Serializable> {
    // 错误信息
    int NULL_INSTANCE = 0;
    int REMOTE_VERIFY_ERROR = 1;

    /**
     * 本地发生异常时候调用
     * @param errorSign -- 上面的提示码或者i18n
     */
    void onError(TransportSceneData<K> request, int errorSign);

    /**
     * 切换场景
     * @param connect -- 目标节点
     * @param request -- 发起者唯一标识
     */
     void transport(SceneConnect connect, TransportSceneData<K> request, boolean local);

    /**
     * 对应的策略标识
     * @return
     */
    int strategy();
}
