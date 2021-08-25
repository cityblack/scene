package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.ConnectEvent;
import com.lzh.game.scene.common.connect.ConnectFactory;
import com.lzh.game.scene.common.connect.ConnectManage;
import com.lzh.game.scene.common.utils.EventBusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class SofaConnectConnectedEvent implements ConnectionEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SofaConnectConnectedEvent.class);

    private ConnectFactory connectFactory;

    public SofaConnectConnectedEvent(ConnectFactory connectFactory) {
        this.connectFactory = connectFactory;
    }

    @Override
    public void onEvent(String remoteAddr, Connection conn) {
        Connect connect = (Connect) conn.getAttribute(ContextConstant.SCENE_CONNECT_RELATION);
        if (Objects.isNull(connect)) {
            connect = connectFactory.createConnect(remoteAddr, conn);
            logger.info("Create connect [{}]!!", connect.key());
        }
        ConnectEvent event = new ConnectEvent();
        event.setConnect(connect);
        event.setType(ConnectEvent.CONNECTED);
        EventBusUtils.getInstance().post(event);
    }
}
