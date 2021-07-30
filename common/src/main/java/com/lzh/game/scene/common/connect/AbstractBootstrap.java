package com.lzh.game.scene.common.connect;

import com.lzh.game.scene.common.connect.codec.ProtostuffSerializer;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.common.connect.server.CmdClassManage;
import com.lzh.game.scene.common.connect.server.InvokeManage;
import com.lzh.game.scene.common.connect.server.RequestHandler;
import com.lzh.game.scene.common.connect.server.ServerMessageManage;
import com.lzh.game.scene.common.connect.sofa.SofaRequestHandler;
import com.lzh.game.scene.common.connect.sofa.SofaRpcSerializationRegister;
import com.lzh.game.scene.common.connect.sofa.SofaUserProcess;

import java.util.Objects;

/**
 * 双工通信 抽出服务端和客户端相同的
 */
public abstract class AbstractBootstrap {

    // 序列化注册
    static {
        SofaRpcSerializationRegister.registerCustomSerializer();
    }

    private ConnectFactory connectFactory;

    private SofaUserProcess sofaUserProcess;

    private RequestHandler requestHandler;

    private DefaultConnectManage connectManage;

    private CmdClassManage classManage;

    private InvokeManage invokeManage;

    private Serializer serializer;

    protected void build() {
        if (Objects.isNull(connectFactory)) {
            this.connectFactory = getDefaultFactory();
            if (Objects.isNull(connectFactory)) {
                throw new IllegalArgumentException("ConnectFactory is null");
            }
        }
        if (Objects.isNull(invokeManage) || Objects.isNull(classManage)) {
            ServerMessageManage messageManage = new ServerMessageManage();
            this.invokeManage = messageManage;
            this.classManage = messageManage;
        }
        if (Objects.isNull(requestHandler)) {
            this.requestHandler = new SofaRequestHandler(this.invokeManage);
        }
        if (Objects.isNull(sofaUserProcess)) {
            this.sofaUserProcess = new SofaUserProcess(this.requestHandler);
        }
        if (Objects.isNull(serializer)) {
            this.serializer = new ProtostuffSerializer();
        }
        if (Objects.isNull(connectManage)) {
            this.connectManage = new DefaultConnectManage();
        }
        SofaRpcSerializationRegister.setSerialization(getSerializer());
        SofaRpcSerializationRegister.setClassManage(getClassManage());
    }

    public ConnectFactory getConnectFactory() {
        return connectFactory;
    }

    public SofaUserProcess getSofaUserProcess() {
        return sofaUserProcess;
    }

    public void setSofaUserProcess(SofaUserProcess sofaUserProcess) {
        this.sofaUserProcess = sofaUserProcess;
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public DefaultConnectManage getConnectManage() {
        return connectManage;
    }

    public void setConnectManage(DefaultConnectManage connectManage) {
        this.connectManage = connectManage;
    }

    public CmdClassManage getClassManage() {
        return classManage;
    }

    public void setClassManage(CmdClassManage classManage) {
        this.classManage = classManage;
    }

    public InvokeManage getInvokeManage() {
        return invokeManage;
    }

    public void setInvokeManage(InvokeManage invokeManage) {
        this.invokeManage = invokeManage;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public void setConnectFactory(ConnectFactory connectFactory) {
        this.connectFactory = connectFactory;
    }

    protected abstract ConnectFactory getDefaultFactory();
}
