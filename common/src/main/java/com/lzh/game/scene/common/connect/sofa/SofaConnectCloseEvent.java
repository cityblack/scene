package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.connect.ConnectManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SofaConnectCloseEvent implements ConnectionEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SofaConnectCloseEvent.class);

    private ConnectManage connectManage;

    public SofaConnectCloseEvent(ConnectManage connectManage) {
        this.connectManage = connectManage;
    }

    @Override
    public void onEvent(String remoteAddr, Connection conn) {
        String key = (String) conn.getAttribute(ContextConstant.SOURCE_CONNECT_RELATION);
        connectManage.removeConnect(remoteAddr);
        logger.info("Close connect [{}-{}]!!", conn.getUrl(), key);
    }
}
