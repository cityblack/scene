package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.ConnectEvent;
import com.lzh.game.scene.common.connect.ConnectManage;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.utils.EventBusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class SofaConnectCloseEvent implements ConnectionEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SofaConnectCloseEvent.class);

    private ConnectManage<SceneConnect> connectManage;

    public SofaConnectCloseEvent(ConnectManage<SceneConnect> connectManage) {
        this.connectManage = connectManage;
    }

    @Override
    public void onEvent(String remoteAddr, Connection conn) {
        Connect connect = (Connect) conn.getAttribute(ContextConstant.SOURCE_CONNECT_RELATION);
        logger.info("Close connect [{}-{}]!!", conn.getUrl(), connect.key());
        ConnectEvent closeEvent = new ConnectEvent();
        closeEvent.setConnect(connect);
        closeEvent.setType(ConnectEvent.CLOSED);
        EventBusUtils.getInstance().post(closeEvent);

        // 为null的情况可能是节点连接了但是还没注册就关闭了
        String key = connect.getAttr(ContextConstant.SCENE_CONNECT_RELATION);
        if (Objects.nonNull(key)) {
            connectManage.removeConnect(key);
        }
        try {
            connect.close();
        } catch (IOException e) {
            logger.error("Do connection close error!!", e);
        }
    }
}
