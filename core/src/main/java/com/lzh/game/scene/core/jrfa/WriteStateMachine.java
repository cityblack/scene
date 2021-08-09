package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class WriteStateMachine extends StateMachineAdapter {

    public static final Logger logger = LoggerFactory.getLogger(WriteStateMachine.class);

    private Serializer serializer;

    private JRService service;

    public WriteStateMachine(Serializer serializer, JRService service) {
        this.serializer = serializer;
        this.service = service;
    }

    @Override
    public void onApply(Iterator iter) {

        while (iter.hasNext()) {
            Status status = Status.OK();
            WriteClosure writeClosure = null;
            try {
                Closure closure = iter.done();
                if (Objects.nonNull(closure)) {
                    writeClosure = (WriteClosure) closure;
                } else {
                    WriteRequest request = WriteRequest.parseFrom(iter.getData().array());
                    writeClosure = new WriteClosure(null, request, serializer, service);
                }
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                writeClosure.run(status);
                iter.next();
            }
        }
    }
}
