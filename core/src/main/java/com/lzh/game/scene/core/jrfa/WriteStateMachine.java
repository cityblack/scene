package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;

import java.util.Objects;

public class WriteStateMachine extends StateMachineAdapter {

    private Serializer serializer;

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
                    writeClosure = new WriteClosure(null, request, serializer);
                }
            } catch (Exception e) {

            } finally {
                writeClosure.run(status);
            }
        }
    }
}
