package com.lzh.game.scene.common.connect;

import com.lzh.game.scene.common.connect.codec.ProtostuffSerializer;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.connect.server.*;
import com.lzh.game.scene.common.connect.server.cmd.ServerMessageManage;
import com.lzh.game.scene.common.connect.sofa.SofaRequestHandler;
import com.lzh.game.scene.common.connect.sofa.SofaRpcSerializationRegister;
import com.lzh.game.scene.common.connect.sofa.SofaUserProcess;
import com.lzh.game.scene.common.utils.ThreadUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

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

    private ConnectManage<SceneConnect> connectManage;

    private InvokeManage invokeManage;

    private Serializer serializer;

    private MethodInvokeHelper methodInvokeHelper;

    // io 处理线程 最好是自行定义传入
    private Executor ioExecutor;
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
        if (Objects.isNull(serializer)) {
            this.serializer = new ProtostuffSerializer();
        }
        if (Objects.isNull(connectManage)) {
            this.connectManage = new SceneConnectManage();
        }
        if (Objects.isNull(ioExecutor)) {
            this.ioExecutor = ThreadUtils.createIoFixedService("default-io-executor");
        }
        if (Objects.isNull(sofaUserProcess)) {
            this.sofaUserProcess = new SofaUserProcess(this.requestHandler, this.ioExecutor);
        }
        if (Objects.isNull(methodInvokeHelper)) {
            this.methodInvokeHelper = new SimpleInvokeHelper();
        }
        SofaRpcSerializationRegister.setSerialization(getSerializer());
    }

    public Executor getIoExecutor() {
        return ioExecutor;
    }

    public void setIoExecutor(Executor ioExecutor) {
        this.ioExecutor = ioExecutor;
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

    public ConnectManage<SceneConnect> getConnectManage() {
        return connectManage;
    }

    public void setConnectManage(SceneConnectManage connectManage) {
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

    @Override
    public void shutdown() {
        this.connectManage.shutdown();
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
        this.status = STARTED;
        this.doStart();
    }

    protected abstract void doInit();

    protected abstract void doStart();

    protected abstract ConnectFactory getDefaultFactory();
}
