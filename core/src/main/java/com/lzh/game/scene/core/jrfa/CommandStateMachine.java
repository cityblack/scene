package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.lzh.game.scene.common.connect.codec.Serializer;

import java.util.Objects;

public class CommandStateMachine extends StateMachineAdapter {

    private Serializer serializer;

    @Override
    public void onApply(Iterator iter) {

        while (iter.hasNext()) {
            Status status = Status.OK();
            CommandClosure commandClosure = null;
            try {
                Closure closure = iter.done();
                if (Objects.nonNull(closure)) {
                    commandClosure = (CommandClosure) closure;
                } else {

//                    commandClosure = new CommandClosure(null, iter.getData(), serializer);
                }
            } catch (Exception e) {

            } finally {
                commandClosure.run(status);
            }
        }
    }
}
