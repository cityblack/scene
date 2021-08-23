package com.lzh.game.scene.api.server;

import com.google.common.eventbus.Subscribe;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.ConnectEvent;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.common.proto.NodeInfoRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.lzh.game.scene.common.RequestSpace.*;

public class ConnectClientServer {

    private ConnectClient client;

    /**
     * 与调度器连接完毕之后 如果是Client节点 从调度器会获得所有的场景节点进行连接
     * @param event
     */
    @Subscribe
    public void onConnect(ConnectEvent event) {
        if (event.getType() == ConnectEvent.CONNECTED) {
            NodeInfoRequest nodeInfo = new NodeInfoRequest();
            Request request = Request.of(cmd(NODE_SPACE, NODE_REGISTER), nodeInfo);
            CompletableFuture<Response<List<NodeInfo>>> future = event.getConnect().sendMessage(request);

            if (client.nodeType() == NodeType.CLIENT_NODE
                    || client.nodeType() == NodeType.CLIENT_NODE_AND_SCENE_NODE)
            future.thenAccept(response -> {
                if (response.getStatus() == ContextConstant.RIGHT_RESPONSE) {
                    for (NodeInfo info : response.getParam()) {
                        client.createConnect(info.getIp(), info.getPort(), NodeType.SCENE_NODE);
                    }
                }
            });
        }
    }

    private void sendNodeInfo() {

    }

    private NodeType getNodeType() {
        return NodeType.values()[client.getConfig().getNodeType()];
    }
}
