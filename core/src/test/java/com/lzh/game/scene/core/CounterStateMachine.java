package com.lzh.game.scene.core;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.entity.PeerId;
import com.lzh.game.scene.common.connect.codec.Serializer;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterStateMachine extends StateMachineAdapter {

    private Serializer serializer;

    private Node node;

    private PeerId peerId;

    public CounterStateMachine(Serializer serializer) {
        this.serializer = serializer;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setPeerId(PeerId peerId) {
        this.peerId = peerId;
    }

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void onApply(Iterator iter) {
       while (iter.hasNext()) {
           ByteBuffer data = iter.getData();
           int value = serializer.decode(data.array(), Integer.class);
           System.out.println(String.format("当前节点:%s 值:%d count:%d", peerId.toString(), value, count.incrementAndGet()));
           Closure closure = iter.done();
           if (Objects.nonNull(closure)) {
               closure.run(Status.OK());
           }
           iter.next();
       }
    }
}
