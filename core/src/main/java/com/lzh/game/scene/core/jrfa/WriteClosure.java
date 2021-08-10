package com.lzh.game.scene.core.jrfa;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Status;
import com.google.protobuf.Message;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.core.jrfa.rpc.entity.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

public class WriteClosure implements Closure {

    public static final Logger logger = LoggerFactory.getLogger(WriteClosure.class);
    // 这个用于如果本地就是主节点 避免序反列化
    private Serializable data;

    private Message request;

    private Serializer serializer;

    private JRService service;

    public WriteClosure(Serializable data, Message request, Serializer serializer, JRService service) {
        this.data = data;
        this.request = request;
        this.serializer = serializer;
        this.service = service;
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
        AbstractExchangeProcess process = this.service.getProcess(cmd);
        if (Objects.isNull(process)) {
            status.setError(-1, "CMD process not register");
            return;
        }
        try {
            if (Objects.isNull(data)) {
                Class<? extends Serializable> dataType = process.getRequestParamType();
                if (Objects.nonNull(dataType)) {
                    data = serializer.decode(writeRequest.getData().toByteArray(), dataType);
                }
            }
            process.onRequest(cmd, data);
        } catch (Exception e) {
            status.setError(-1, e.getMessage());
        }
    }
}
