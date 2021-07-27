package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.DefaultCustomSerializer;
import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.exception.SerializationException;
import com.alipay.remoting.rpc.RequestCommand;
import com.alipay.remoting.rpc.ResponseCommand;
import com.alipay.remoting.rpc.RpcCommand;
import com.alipay.remoting.rpc.protocol.RpcRequestCommand;

/**
 * header -> short 标识请求唯一
 * context ->
 */
public class SofaRpcSerialization extends DefaultCustomSerializer {

    @Override
    public <T extends ResponseCommand> boolean serializeHeader(T response) throws SerializationException {
        return super.serializeHeader(response);
    }

    @Override
    public <T extends RequestCommand> boolean serializeHeader(T request, InvokeContext invokeContext) throws SerializationException {
        if (request instanceof RpcRequestCommand) {
            RpcRequestCommand command = (RpcRequestCommand) request;
            Object header = command.getRequestHeader();

        }
        return super.serializeHeader(request, invokeContext);
    }
}
