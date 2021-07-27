package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.DefaultCustomSerializer;
import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.exception.SerializationException;
import com.alipay.remoting.rpc.RequestCommand;
import com.alipay.remoting.rpc.ResponseCommand;
import com.alipay.remoting.rpc.RpcCommand;
import com.alipay.remoting.rpc.protocol.RpcRequestCommand;
import com.lzh.game.scene.common.ContextDefined;
import com.lzh.game.scene.common.connect.ExchangeBase;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.codec.Serializer;

/**
 * 节约带宽，按SOFA自身协议 把请求CMD和对应的class信息写到信息头里面
 * 信息的BODY直接写参数进去
 * header -> int 4b 标识请求唯一
 * context ->
 */
public class SofaRpcSerialization extends DefaultCustomSerializer {

    private Serializer serializer;

    @Override
    public <T extends ResponseCommand> boolean serializeHeader(T response) throws SerializationException {
        return super.serializeHeader(response);
    }

    @Override
    public <T extends RequestCommand> boolean serializeHeader(T request, InvokeContext invokeContext) throws SerializationException {
        if (request instanceof RpcRequestCommand) {
            RpcRequestCommand command = (RpcRequestCommand) request;
            Object o = command.getRequestObject();
            if (o instanceof Request) {
                Request r = (Request) o;
                int cmd = r.getId();
                String className = r.getClassName();

            }
        }

        return super.serializeHeader(request, invokeContext);
    }

    @Override
    public <T extends ResponseCommand> boolean serializeContent(T response) throws SerializationException {
        return super.serializeContent(response);
    }

    @Override
    public <T extends RequestCommand> boolean serializeContent(T request, InvokeContext invokeContext) throws SerializationException {
        if (request instanceof RpcRequestCommand) {
            RpcRequestCommand command = (RpcRequestCommand) request;
            Object o = command.getRequestObject();
            if (o instanceof Request) {
                Object value = ((Request)o).getParam();
                byte[] content = serializer.encode(value);
                request.setContent(content);
            }
        }
        return super.serializeContent(request, invokeContext);
    }
}
