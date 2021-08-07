package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Status;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;
import com.lzh.game.scene.core.service.ReplicatorCmd;
import com.lzh.game.scene.core.service.impl.AbstractExchangeProcess;

import java.io.Serializable;
import java.util.Objects;

public class WriteClosure implements Closure {
    // 这个用于如果本地就是主节点 避免序反列化
    private Serializable data;

    private Message request;

    private Serializer serializer;

    public WriteClosure(Serializable data, Message request, Serializer serializer) {
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
        WriteRequest writeRequest = (WriteRequest) request;
        int key = writeRequest.getKey();
        ReplicatorCmd cmd = ReplicatorCmd.of(key);
        if (Objects.isNull(cmd)) {
            status.setError(-1,"%s:%d", "CMD not register", key);
            return;
        }
        AbstractExchangeProcess process = cmd.getProcess();
        if (Objects.isNull(data)) {
            Class<? extends Serializable> dataType = process.getRequestParamType();
            if (Objects.nonNull(dataType)) {
                data = serializer.decode(writeRequest.toByteArray(), dataType);
            }
        }
        try {
            process.onRequest(cmd, data);
        } catch (Exception e) {
            status.setError(-1, e.getMessage());
        }
    }
}
