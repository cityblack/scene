package com.lzh.game.scene.core;

import com.alipay.sofa.jraft.*;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcServer;
import com.lzh.game.scene.common.connect.codec.ProtostuffSerializer;
import com.lzh.game.scene.common.connect.codec.Serializer;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class CoreAppTest {

    @Test
    public void map() {
        Map<String, List<Integer>> map = new HashMap<>();
        List<Integer> list = map.computeIfAbsent("1", (k) -> new ArrayList<>());
        list.add(1);
        System.out.println(map.get("1"));
    }

    @Test
    public void nodeTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Configuration conf = JRaftUtils.getConfiguration("localhost:8081,localhost:8082,localhost:8083");

//        for (PeerId id : conf.getPeers()) {
//            nodeManager.addAddress(id.getEndpoint());
//            RpcServer rpcServer = RaftRpcServerFactory.createAndStartRaftRpcServer(id.getEndpoint());
//            rpcServer.init(null);
//        }
        Serializer serializer = new ProtostuffSerializer();

        NodeOptions options = new NodeOptions();
//        options.setLogUri("/Users/jsonp/Documents/logs/scene");
//        options.setRaftMetaUri("/Users/jsonp/Documents/mate");
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
                System.out.println("node:" + peerId.toString() + " -> "+ object);
            }
        }
    }
}
