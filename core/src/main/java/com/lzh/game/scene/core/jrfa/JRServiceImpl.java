package com.lzh.game.scene.core.jrfa;

import com.alipay.remoting.rpc.RpcServer;
import com.alipay.sofa.jraft.JRaftUtils;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.StateMachine;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.impl.BoltRpcServer;
import com.alipay.sofa.jraft.util.Endpoint;
import com.google.protobuf.Message;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.ClusterServerConfig;
import com.lzh.game.scene.core.exchange.DataRequest;
import com.lzh.game.scene.core.exchange.DataResponse;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class JRServiceImpl implements JRService {

    private static final String JR_GROUP = "scene_manage_cluster_group";
    // 当前服节点
    private Node node;

    private RpcServer rpcServer;

    private RaftGroupService groupService;

    private NodeOptions options;

    private StateMachine stateMachine;

    private Configuration configuration;

    private Serializer serializer;

    public JRServiceImpl(RpcServer rpcServer, Serializer serializer) {
        this.rpcServer = rpcServer;
        this.serializer = serializer;
    }

    @Override
    public <T extends ClusterServerConfig> void start(T config) {

        List<String> list = config.getCluster();
        Configuration conf = JRaftUtils.getConfiguration(String.join(",", list));
        if (conf.isValid()) {
            throw new IllegalArgumentException("JRfa config error!!");
        }
        this.build();

        NodeOptions options = new NodeOptions();
        options.setLogUri(config.getConsistLogUri());
        options.setRaftMetaUri(config.getMetaUri());
        options.setFsm(stateMachine);

        Endpoint addr = new Endpoint(config.getIp(), config.getPort());
        PeerId id = PeerId.parsePeer(addr.toString());
        RaftGroupService service = new RaftGroupService(JR_GROUP, id, options, new BoltRpcServer(this.rpcServer));
        Node node = service.start();

        this.groupService = service;
        this.configuration = conf;
        this.options = options;
        this.node = node;
    }

    @Override
    public CompletableFuture<Response> commitTask(DataRequest request) {
        if (node.isLeader()) {

        } else {

        }
        return null;
    }

    /*protected void applyOperation(Node node, Message data, FailoverClosure closure) {
        final Task task = new Task();
        task.setDone(new NacosClosure(data, status -> {
            NacosClosure.NacosStatus nacosStatus = (NacosClosure.NacosStatus) status;
            closure.setThrowable(nacosStatus.getThrowable());
            closure.setResponse(nacosStatus.getResponse());
            closure.run(nacosStatus);
        }));
        task.setData(ByteBuffer.wrap(data.toByteArray()));
        node.apply(task);
    }*/

    @Override
    public void onRequest(DataRequest request) {

    }

    @Override
    public void onResponse(DataResponse response) {

    }

    @Override
    public Serializer serializer() {
        return serializer;
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    protected void build() {
        if (Objects.isNull(this.stateMachine)) {
            this.stateMachine = new CommandStateMachine();
        }
    }
}
