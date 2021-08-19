package com.lzh.game.scene.common.connect;

import com.lzh.game.scene.common.connect.codec.ProtostuffSerializer;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.common.connect.server.*;
import com.lzh.game.scene.common.connect.server.cmd.ServerMessageManage;
import com.lzh.game.scene.common.connect.sofa.SofaRequestHandler;
import com.lzh.game.scene.common.connect.sofa.SofaRpcSerializationRegister;
import com.lzh.game.scene.common.connect.sofa.SofaUserProcess;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 双工通信 抽出服务端和客户端相同的
 */
public abstract class AbstractBootstrap<T extends BootstrapConfig> implements Bootstrap<T> {

    private final static int NOT_INIT = 0;

    private final static int INIT = 1;

    private final static int STARTED = 2;

    private volatile int status = NOT_INIT;
    // 序列化注册
    static {
        SofaRpcSerializationRegister.registerCustomSerializer();
    }

    private T config;

    public AbstractBootstrap(T config) {
        this.config = config;
    }

    private ConnectFactory connectFactory;

    private SofaUserProcess sofaUserProcess;

    private RequestHelper requestHelper;

    private RequestHandler requestHandler;

    private DefaultConnectManage connectManage;

    private InvokeManage invokeManage;

    private Serializer serializer;

    private MethodInvokeHelper methodInvokeHelper;
    /**
     * Build Default bean
     */
    protected void build() {
        if (Objects.isNull(connectFactory)) {
            this.connectFactory = getDefaultFactory();
            if (Objects.isNull(connectFactory)) {
                throw new NullPointerException("ConnectFactory is null");
            }
        }
        if (Objects.isNull(invokeManage)) {
            ServerMessageManage messageManage = new ServerMessageManage();
            this.invokeManage = messageManage;
        }
        if (Objects.isNull(requestHelper)) {
            this.requestHelper = new RequestHelperImpl();
        }
        if (Objects.isNull(requestHandler)) {
            this.requestHandler = new SofaRequestHandler(this.invokeManage, this.requestHelper);
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
        if (Objects.isNull(methodInvokeHelper)) {
            this.methodInvokeHelper = new SimpleInvokeHelper(this.requestHelper);
        }
        SofaRpcSerializationRegister.setSerialization(getSerializer());
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

    public RequestHelper getRequestHelper() {
        return requestHelper;
    }

    public void setRequestHelper(RequestHelper requestHelper) {
        this.requestHelper = requestHelper;
    }

    public MethodInvokeHelper getMethodInvokeFactory() {
        return methodInvokeHelper;
    }

    public void setMethodInvokeFactory(MethodInvokeHelper methodInvokeHelper) {
        this.methodInvokeHelper = methodInvokeHelper;
    }

    @Override
    public T getConfig() {
        return config;
    }

    public void setConfig(T config) {
        this.config = config;
    }

    public void addCmdTarget(List<Object> targets) {
        this.methodInvokeHelper.addMethodInvoke(this.invokeManage, targets);
    }

    public void init() {
        this.config.check();
        this.build();
        this.doInit();
        this.status = INIT;
    }

    @Override
    public void start() {
        if (this.status == STARTED) {
            return;
        }
        if (this.status != INIT) {
            throw new RuntimeException("Bootstrap not init..");
        }
        this.doStart();
        this.status = STARTED;
    }

    protected abstract void doInit();

    protected abstract void doStart();

    protected abstract ConnectFactory getDefaultFactory();
}
