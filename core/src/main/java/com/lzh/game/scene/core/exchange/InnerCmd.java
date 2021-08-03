package com.lzh.game.scene.core.exchange;

import com.lzh.game.scene.common.SceneInstance;

public enum InnerCmd {
    // 新增
    ADD_INSTANCE(SceneInstance.class),
    // 移除
    REMOVE_INSTANCE(Long.class),

    ;
    private Class<?> paramClass;

    InnerCmd(Class<?> paramClass) {
        this.paramClass = paramClass;
    }
}
