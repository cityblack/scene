package com.lzh.game.scene.api.server;

import com.google.common.eventbus.Subscribe;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.ConnectEvent;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.common.proto.NodeInfoRequest;
import com.lzh.game.scene.common.utils.EventBusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.lzh.game.scene.common.RequestSpace.*;

public class ConnectClientServer {

    private static final Logger logger = LoggerFactory.getLogger(ConnectClientServer.class);

    private ConnectClient client;

    public void setClient(ConnectClient client) {
        this.client = client;
    }

    /**
     * 与调度器连接完毕之后 如果是Client节点 从调度器会获得所有的场景节点进行连接
     *
     * @param event
     */
    @Subscribe
    public void onConnect(ConnectEvent event) {
        if (event.getType() == ConnectEvent.CONNECTED) {
            NodeInfoRequest nodeInfo = new NodeInfoRequest();
            nodeInfo.setType(client.getConfig().getDefinedNodeType());
            nodeInfo.setWeight(0);

            Request request = Request.of(cmd(NODE_SPACE, NODE_REGISTER), nodeInfo);
            CompletableFuture<Response<List<NodeInfo>>> future = event.getConnect().sendMessage(request);

            if (client.nodeType() == NodeType.CLIENT_NODE
                    || client.nodeType() == NodeType.CLIENT_NODE_AND_SCENE_NODE) {
                // 获取所有的场景节点进行连接
                try {
                    Response<List<NodeInfo>> response = future.get(5000, TimeUnit.SECONDS);
                    logger.info("注册完毕后收到服务端数据:{}", response.getParam());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                /*future.thenAccept(response -> {
                    if (response.getStatus() == ContextConstant.RIGHT_RESPONSE) {
                        logger.info("注册完毕后收到服务端数据:{}", response.getParam());
//                        for (NodeInfo info : response.getParam()) {
//                            String address = info.getIp() + ":" + info.getPort();
//                            String key = SceneConnect.TO_UNIQUE.apply(address, NodeType.SCENE_NODE);
//                            // 防止重复连接
//                            if (client.connectManage().contain(key)) {
//                                continue;
//                            }
//                            client.createConnect(info.getIp(), info.getPort(), NodeType.SCENE_NODE);
//                        }
                    }
                });*/
            }

        }
    }

    private NodeType getNodeType() {
        return NodeType.values()[client.getConfig().getNodeType()];
    }

    {
        EventBusUtils.getInstance().register(this);
    }
}
