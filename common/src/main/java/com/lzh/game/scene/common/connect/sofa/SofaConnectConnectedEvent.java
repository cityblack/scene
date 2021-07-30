package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.ConnectFactory;
import com.lzh.game.scene.common.connect.ConnectManage;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.scene.SceneConnectManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SofaConnectConnectedEvent implements ConnectionEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SofaConnectConnectedEvent.class);

    private ConnectManage connectManage;

    private ConnectFactory connectFactory;

    public SofaConnectConnectedEvent(ConnectManage connectManage, ConnectFactory connectFactory) {
        this.connectManage = connectManage;
        this.connectFactory = connectFactory;
    }

    @Override
    public void onEvent(String remoteAddr, Connection conn) {
        Connect connect = connectFactory.createConnect(remoteAddr, conn);
        connectManage.putConnect(connect.key(), connect);
        logger.info("Remote {} connected!!!", remoteAddr);
    }
}
