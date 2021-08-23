package com.lzh.game.scene.core.controller;

import static com.lzh.game.scene.common.RequestSpace.*;

import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.server.cmd.Action;
import com.lzh.game.scene.common.connect.server.cmd.Cmd;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.common.proto.NodeInfoRequest;
import com.lzh.game.scene.core.node.NodeService;

import java.util.List;

@Action(NODE_SPACE)
public class NodeController {

    private NodeService nodeService;

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @Cmd(NODE_REGISTER)
    public List<NodeInfo> register(Connect connect, NodeInfoRequest request) {
        return nodeService.register(connect, request);
    }
}
