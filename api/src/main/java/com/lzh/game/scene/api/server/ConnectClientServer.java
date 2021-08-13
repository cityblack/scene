package com.lzh.game.scene.api.server;

import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.proto.NodeInfoRequest;

import java.util.concurrent.CompletableFuture;

import static com.lzh.game.scene.common.RequestSpace.*;

public class ConnectClientServer {

    private ConnectClient client;

    public void onConnect(ConnectEvent event) {
        if (event.getType() == ConnectEvent.CONNECTED) {
            NodeInfoRequest nodeInfo = new NodeInfoRequest();
            Request request = Request.of(cmd(NODE_SPACE, 1), nodeInfo);
            CompletableFuture<Response<Object>> future = event.getConnect().sendMessage(request);
            future.thenAccept(response -> {
                if (response.getStatus() == ContextConstant.RIGHT_RESPONSE) {

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
