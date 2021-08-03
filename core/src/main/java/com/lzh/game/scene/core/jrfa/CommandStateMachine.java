package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.core.StateMachineAdapter;

import java.util.Objects;

public class CommandStateMachine extends StateMachineAdapter {

    private JRService jrService;

    @Override
    public void onApply(Iterator iter) {

        while (iter.hasNext()) {
            Closure closure = iter.done();
            // 这里说明当前节点是主节点
            if (Objects.isNull(closure)) {

            } else {

//                jrService.onRequest();
            }
        }
    }
}
