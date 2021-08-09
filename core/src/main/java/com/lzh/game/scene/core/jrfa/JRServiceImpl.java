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
import com.lzh.game.scene.core.jrfa.rpc.ReplicatorImpl;
import com.lzh.game.scene.core.jrfa.rpc.WriteRequestProcess;
import com.lzh.game.scene.core.jrfa.rpc.entity.Response;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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

    private Replicator replicator;

    private int invokeOutTime;

    private Map<ReplicatorCmd, AbstractExchangeProcess> processes = new ConcurrentHashMap<>(8);

    public JRServiceImpl(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public <T extends ClusterServerConfig> void start(T config) {

        List<String> list = config.getCluster();
        Configuration conf = JRaftUtils.getConfiguration(String.join(",", list));
        if (!conf.isValid()) {
            throw new IllegalArgumentException("JRaft config error!!");
        }
        this.build();

        NodeOptions options = new NodeOptions();
        options.setLogUri(config.getConsistLogUri());
        options.setRaftMetaUri(config.getMetaUri());
        options.setFsm(stateMachine);
        options.setInitialConf(conf);

        Endpoint addr = new Endpoint(config.getIp(), config.getPort());
        PeerId id = PeerId.parsePeer(addr.toString());
        RaftGroupService service =
                new RaftGroupService(JR_GROUP, id, options);
        Node node = service.start();

        CliOptions cliOptions = new CliOptions();
        CliService cliService = RaftServiceFactory.createAndInitCliService(cliOptions);
//        RouteTable.getInstance().updateConfiguration(JR_GROUP, conf);
        this.rpcServer = ((BoltRpcServer)service.getRpcServer()).getServer();
        this.clientService = ((CliServiceImpl) cliService).getCliClientService();
        this.groupService = service;
        this.configuration = conf;
        this.options = options;
        this.node = node;
        this.invokeOutTime = config.getInvokeOutTime();
        this.rpcServer.registerUserProcessor(new WriteRequestProcess(this));

    }

    @Override
    public <T extends Serializable, R extends WriteRequest> CompletableFuture<Void> commitWrite(T data, R request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (isLeader()) {
            this.applyOperation(data, request, future);
        } else {
            this.invokeToLeader(request, this.invokeOutTime, future);
        }
        return future;
    }

    protected <P> void invokeToLeader(
            final Message request, final int timeoutMillis, final CompletableFuture<P> future) {
        final PeerId peerId = getLeader();
        if (Objects.isNull(peerId)) {
            future.completeExceptionally(new NoLeaderException("Can't find leader!!"));
            return;
        }
        final Endpoint leaderIp = peerId.getEndpoint();
        RpcResponseClosure<Response> done = new FutureClosure<>(future);
        this.clientService.invokeWithDone(leaderIp, request, done, timeoutMillis);
    }

    protected PeerId getLeader() {
        return this.node.getLeaderId();
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
    public <R extends WriteRequest> void leaderWriteInvoke(R request, CompletableFuture<Void> future) {
        this.applyOperation(null, request, future);
    }

    @Override
    public Replicator replicator() {
        return this.replicator;
    }

    @Override
    public RpcServer rpcServer() {
        return this.rpcServer;
    }

    @Override
    public AbstractExchangeProcess getProcess(ReplicatorCmd cmd) {
        return processes.get(cmd);
    }

    @Override
    public void addRequestProcess(ReplicatorCmd cmd, AbstractExchangeProcess process) {
        this.processes.put(cmd, process);
    }

    @Override
    public Serializer serializer() {
        return serializer;
    }

    private <T extends Serializable, R extends Message, P extends Object>
        void applyOperation(T data, R request, CompletableFuture<Void> future) {

        Task task = new Task();
        task.setData(ByteBuffer.wrap(request.toByteArray()));
        WriteClosure closure = new WriteClosure(data, request, serializer, this) {
            @Override
            public void run(Status status) {
                super.run(status);
                if (!status.isOk()) {
                    future.completeExceptionally(new IllegalArgumentException(status.getErrorMsg()));
                }
            }
        };
        task.setDone(closure);
        this.node.apply(task);
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public void setReplicator(Replicator replicator) {
        this.replicator = replicator;
    }

    protected void build() {
        if (Objects.isNull(this.stateMachine)) {
            this.stateMachine = new WriteStateMachine(this.serializer, this);
        }
        if (Objects.isNull(this.replicator)) {
            this.replicator = new ReplicatorImpl(this);
        }
    }
}
