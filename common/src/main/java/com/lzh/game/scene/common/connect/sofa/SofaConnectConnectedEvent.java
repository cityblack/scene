package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;

public class SofaConnectConnectedEvent implements ConnectionEventProcessor {

    private SceneConnectManage<SceneConnect> connectManage;

    @Override
    public void onEvent(String remoteAddr, Connection conn) {
//        SceneConnect connect = new SofaSceneConnect();
    }
}
