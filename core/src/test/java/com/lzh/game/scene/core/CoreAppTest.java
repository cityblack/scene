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
import com.lzh.game.scene.core.jrfa.ReplicatorCmd;
import com.lzh.game.scene.core.jrfa.process.SceneInstanceProcess;
import com.lzh.game.scene.core.service.JRafClusterServer;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import com.lzh.game.scene.core.service.impl.SceneInstanceManageImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class CoreAppTest {

    public final static Logger logger = LoggerFactory.getLogger(CoreAppTest.class);

    public List<JRafClusterServer> cluster() {
        ClusterServerConfig config = new ClusterServerConfig();
        config.getCluster().add("localhost:8081");
        config.getCluster().add("localhost:8082");
        config.getCluster().add("localhost:8083");
        List<JRafClusterServer> servers = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            config.setPort(8081 + i);
            config.setConsistLogUri("classpath:logs" + File.separator + config.getPort());
            config.setMetaUri("classpath:scene" + File.separator + config.getPort());
            config.setSnapshotUri("classpath:snapshot" + File.separator + config.getPort());
            JRafClusterServer<ClusterServerConfig> server = new JRafClusterServer<>(config);
            server.start();
            servers.add(server);
            final SceneInstanceManageImpl manage = new SceneInstanceManageImpl();
            SceneInstanceProcess sceneInstanceProcess = new SceneInstanceProcess(manage);
            server.getJrService().addRequestProcess(ReplicatorCmd.REGISTER_SCENE, sceneInstanceProcess);
            new Thread(() -> {
                while (true) {
                    CompletableFuture.supplyAsync(() -> {
                        List<SceneInstance> list = manage.get("group");
                        if (!list.isEmpty()) {
                            logger.info("当前节点:{} leader:{} scene数量:{}"
                                    , server.getJrService().node().getNodeId()
                                    , server.getJrService().node().isLeader()
                                    , list.size());

                        }
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
        List<JRafClusterServer> servers = cluster();
        while (true) {
            for (JRafClusterServer server: servers) {
                JRService jrService = server.getJrService();
                if (jrService.node().getLeaderId() == null) {
                    continue;
                }
                logger.info("当前节点:{}申请写入实例", server.getJrService().node().getNodeId().getPeerId());
                SceneInstance sceneInstance = new SceneInstance();
                sceneInstance.setGroup("group");
                sceneInstance.setMap(1);
                sceneInstance.setUnique(String.valueOf(count.getAndIncrement()));
                jrService
                        .replicator()
                        .registerSceneInstance(sceneInstance)
                        .exceptionally(throwable -> {
                            throwable.printStackTrace();
                            return null;
                        });
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
                            node.apply(task);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
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
                logger.info("当前节点:{}", peerId.toString());
            }
        }
    }

    @Test
    public void insert() {
        SceneInstanceManage manage = new SceneInstanceManageImpl();
        for (int i = 0; i < 500000; i++) {
            SceneInstance instance = new SceneInstance();
//        int count = ps.count.incrementAndGet();
            instance.setMap(1);
            instance.setUnique("3");
            instance.setGroup("3");
            manage.put("3", instance);
        }
    }
}
