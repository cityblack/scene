package com.lzh.game.scene.core.jrfa;

import com.alipay.remoting.rpc.RpcServer;
import com.alipay.sofa.jraft.*;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.core.CliServiceImpl;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.ClientService;
import com.alipay.sofa.jraft.rpc.RpcResponseClosure;
import com.alipay.sofa.jraft.rpc.impl.BoltRpcServer;
import com.alipay.sofa.jraft.util.Endpoint;
import com.google.protobuf.Message;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.ClusterServerConfig;
import com.lzh.game.scene.core.exception.NoLeaderException;

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

    private ClientService clientService;

    private int invokeOutTime;

    public JRServiceImpl(RpcServer rpcServer, Serializer serializer) {
        this.rpcServer = rpcServer;
        this.serializer = serializer;
    }

    @Override
    public <T extends ClusterServerConfig> void start(T config) {

        List<String> list = config.getCluster();
        Configuration conf = JRaftUtils.getConfiguration(String.join(",", list));
        if (conf.isValid()) {
            throw new IllegalArgumentException("JRaft config error!!");
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

        CliOptions cliOptions = new CliOptions();
        CliService cliService = RaftServiceFactory.createAndInitCliService(cliOptions);

        this.clientService = ((CliServiceImpl)cliService).getCliClientService();
        this.groupService = service;
        this.configuration = conf;
        this.options = options;
        this.node = node;
        this.invokeOutTime = config.getInvokeOutTime();
    }

    @Override
    public <E extends Message, T extends Message> CompletableFuture<E> commitTask(T request) {
        CompletableFuture<E> future = new CompletableFuture<>();
        if (isLeader()) {
            applyOperation(this.node, request, future);
        } else {
            invokeToLeader(request, invokeOutTime, future);
        }
        return future;
    }

    protected <T extends Message> void invokeToLeader(final Message request, final int timeoutMillis, final CompletableFuture<T> future) {
        final PeerId peerId = getLeader();
        if (Objects.isNull(peerId)) {
            future.completeExceptionally(new NoLeaderException("Can't find leader!!"));
            return;
        }
        final Endpoint leaderIp = peerId.getEndpoint();
        RpcResponseClosure<T> done = new FutureClosure<>(future);
        this.clientService.invokeWithDone(leaderIp, request, done, timeoutMillis);
    }

    protected PeerId getLeader() {
        return this.node.getLeaderId();
    }

    @Override
    public <T extends Message, R extends Message>void applyOperation(Node node, T data, final CompletableFuture<R> future) {
        final Task task = new Task();
        task.setData(ByteBuffer.wrap(data.toByteArray()));
        task.setDone(new Closure() {
            @Override
            public void run(Status status) {
                if (status.isOk()) {
//                    future.complete()
                }
            }
        });
//        task.setDone(new NacosClosure(data, status -> {
//            NacosClosure.NacosStatus nacosStatus = (NacosClosure.NacosStatus) status;
//            closure.setThrowable(nacosStatus.getThrowable());
//            closure.setResponse(nacosStatus.getResponse());
//            closure.run(nacosStatus);
//        }));
//        task.setData(data.);
        node.apply(task);
    }

    @Override
    public boolean isLeader() {
        return this.node.isLeader();
    }

    @Override
    public Node node() {
        return this.node;
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
