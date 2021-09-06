package com.lzh.game.scene.api.scene;

/**
 * 切换场景 远程端
 */
public interface TransportRemote<V, R> {

    /**
     * 远程端验证是否能进入
     * @param verifyRequest
     * @return -- 0 表示成功 否则返回对应的i18n
     */
    int verify(V verifyRequest);

    /**
     * 进入场景
     * @param requestData
     */
    void enterScene(R requestData);

    /**
     * 对应的策略标识
     * @return
     */
    int strategy();
}
