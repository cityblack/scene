package com.lzh.game.scene.core;

import com.alipay.sofa.jraft.*;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.codec.ProtostuffSerializer;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.jrfa.JRService;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.service.SofaClusterServer;
import com.lzh.game.scene.core.service.impl.SceneInstanceManageImpl;
import com.lzh.game.scene.core.service.impl.process.SceneInstanceProcess;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class CoreAppTest {

    public final static Logger logger = LoggerFactory.getLogger(CoreAppTest.class);

    public List<SofaClusterServer> cluster() {
        ClusterServerConfig config = new ClusterServerConfig();
        config.getCluster().add("localhost:8081");
        config.getCluster().add("localhost:8082");
        config.getCluster().add("localhost:8083");
        List<SofaClusterServer> servers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            config.setPort(8081 + i);
            config.setConsistLogUri("/Users/jsonp/Documents/logs/scene/" + config.getPort());
            config.setMetaUri("/Users/jsonp/Documents/mate/" + config.getPort());
            SofaClusterServer<ClusterServerConfig> server = new SofaClusterServer<>(config);
            server.start();
            servers.add(server);
            final SceneInstanceManage manage = new SceneInstanceManageImpl();
            SceneInstanceProcess sceneInstanceProcess = new SceneInstanceProcess(manage);
            server.getJrService().addRequestProcess(sceneInstanceProcess);

            new Thread(() -> {
                while (true) {
                    CompletableFuture.supplyAsync(() -> {
                        List<SceneInstance> list = manage.get("group");
                        logger.info("当前节点:{} scene数量:{}", server.getJrService().node().getNodeId(), list.size());
                        return null;
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return servers;
    }

    @Test
    public void start() {
        AtomicInteger count = new AtomicInteger(1);
        List<SofaClusterServer> servers = cluster();
        while (true) {
            for (SofaClusterServer server: servers) {
                JRService jrService = server.getJrService();
                if (jrService.node().getLeaderId() == null) {
                    continue;
                }
                if (!jrService.isLeader()) {
                    SceneInstance sceneInstance = new SceneInstance();
                    sceneInstance.setGroup("group");
                    sceneInstance.setMap(1);
                    sceneInstance.setUnique(String.valueOf(count.getAndIncrement()));
                    jrService
                            .replicator()
                            .registerSceneInstance(sceneInstance);
//                    .exceptionally(throwable -> {
//                        throwable.printStackTrace();
//                        return null;
//                    });
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void map() {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        future.exceptionally(throwable -> {
            System.out.println("??");
            return 1;
        }).thenAccept(k -> {
            System.out.println("??x");
        });
        future.complete(1);
//        future.completeExceptionally(new RuntimeException());

    }

    @Test
    public void nodeTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Configuration conf = JRaftUtils.getConfiguration("localhost:8081,localhost:8082,localhost:8083");

        Serializer serializer = new ProtostuffSerializer();

        NodeOptions options = new NodeOptions();
        options.setInitialConf(conf);

        String groupId = "jraft";
        int i = 0;
        for (PeerId id : conf.getPeers()) {
            CounterStateMachine machine = new CounterStateMachine(serializer);
            options.setFsm(machine);

            i++;
            options.setLogUri("/Users/jsonp/Documents/logs/scene/" + i);
            options.setRaftMetaUri("/Users/jsonp/Documents/mate/" + i);
//            nodeManager.addAddress(id.getEndpoint());
//            RpcServer rpcServer = new RpcServer();

            RaftGroupService service = new RaftGroupService(groupId, id, options);
            Node node = service.start();
            machine.setNode(node);
            machine.setPeerId(id);
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(4000);
                        if (node.isLeader()) {
                            Task task = new Task();
                            task.setData(ByteBuffer.wrap(serializer.encode(5)));
                            task.setDone(new TestClosure(5, id));
//                            task.setExpectedTerm(TimeUnit.SECONDS.toMillis(5));
                            node.apply(task);
                        }
//                        System.out.println("leader" + node.getLeaderId());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            /*Task task = new Task();
            task.setData(ByteBuffer.wrap(serializer.encode(5)));
            task.setDone(new Closure() {
                @Override
                public void run(Status status) {
                    System.out.println(status.getCode());
                    System.out.println(status.getErrorMsg());
                }
            });
            task.setExpectedTerm(TimeUnit.SECONDS.toMillis(5));
            node.apply(task);
            service.getRpcServer();*/
//            nodeManager.add(node);
        }

        latch.await();
    }

    public static class TestClosure implements Closure {

        private Object object;

        private PeerId peerId;

        public TestClosure(Object object, PeerId peerId) {
            this.object = object;
            this.peerId = peerId;
        }

        @Override
        public void run(Status status) {
            if (status.isOk()) {
//                System.out.println("node:" + peerId.toString() + " -> "+ object);
            }
        }
    }
}
