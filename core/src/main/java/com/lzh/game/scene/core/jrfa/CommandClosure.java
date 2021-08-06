package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcResponseClosureAdapter;
import com.google.protobuf.Message;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.jrfa.rpc.entity.Response;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;
import com.lzh.game.scene.core.service.ReplicatorCmd;
import com.lzh.game.scene.core.service.impl.AbstractWriteProcess;

import java.io.Serializable;
import java.util.Objects;

public class CommandClosure extends RpcResponseClosureAdapter<Response> {
    // 这个用于如果本地就是主节点 避免序反列化
    private Serializable data;

    private Message request;

    private Serializer serializer;

    public CommandClosure(Serializable data, Message request, Serializer serializer) {
        this.data = data;
        this.request = request;
        this.serializer = serializer;
    }

    public Object getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    @Override
    public void run(Status status) {
        if (request instanceof WriteRequest) {
            WriteRequest write = (WriteRequest) request;
            int key = write.getKey();
            ReplicatorCmd cmd = ReplicatorCmd.of(key);
            if (Objects.isNull(cmd)) {
                status.setError(-1,"%s:%d", "CMD not register", key);
                return;
            }
            AbstractWriteProcess process = cmd.getProcess();
            if (Objects.isNull(data)) {
                Class<? extends Serializable> dataType = process.getClazz();
                data = serializer.decode(write.toByteArray(), dataType);
            }
            process.onRequest(cmd, data);
        }
    }
}
