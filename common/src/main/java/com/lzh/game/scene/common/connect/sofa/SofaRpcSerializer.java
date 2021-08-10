package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.DefaultCustomSerializer;
import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.exception.DeserializationException;
import com.alipay.remoting.exception.SerializationException;
import com.alipay.remoting.rpc.RequestCommand;
import com.alipay.remoting.rpc.ResponseCommand;
import com.alipay.remoting.rpc.protocol.RpcRequestCommand;
import com.alipay.remoting.rpc.protocol.RpcResponseCommand;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.common.connect.server.cmd.CmdClassManage;

import java.util.Objects;

/**
 * 节约带宽，按SOFA自身协议 把请求CMD和对应的class信息写到信息头里面
 * 信息的BODY直接写参数进去
 * request header -> int 标识请求唯一, 一般压缩之后小于4bit
 * <p>
 * response header -> 返回分两种, 一种是正确的值, 一种是错误信息. 用1位字节标识是否正确响应
 */
public class SofaRpcSerializer extends DefaultCustomSerializer {

    private static final byte RIGHT_RESPONSE = 0x0;

    private static final byte ERROR_RESPONSE = 0x1;

    private Serializer serializer;

    private CmdClassManage classManage;

    public SofaRpcSerializer() {
    }

    public SofaRpcSerializer(Serializer serializer, CmdClassManage classManage) {
        this.serializer = serializer;
        this.classManage = classManage;
    }

    @Override
    public <T extends ResponseCommand> boolean serializeHeader(T response) throws SerializationException {
        if (response instanceof RpcResponseCommand) {
            RpcResponseCommand command = (RpcResponseCommand) response;
            Object o = command.getResponseObject();
            if (o instanceof Response) {
                Response r = (Response) o;
                byte sign = Objects.isNull(r.getError()) ? RIGHT_RESPONSE : ERROR_RESPONSE;
                command.setHeader(new byte[] { sign });
                return true;
            }
        }
        return super.serializeHeader(response);
    }

    @Override
    public <T extends ResponseCommand> boolean deserializeHeader(T response, InvokeContext invokeContext) throws DeserializationException {
        if (response instanceof RpcResponseCommand) {
            RpcResponseCommand command = (RpcResponseCommand) response;
            byte[] values = command.getHeader();
            if (values.length < 0) {
                command.setResponseHeader(ERROR_RESPONSE);
            } else {
                command.setResponseHeader(values[0]);
            }
            return true;
        }
        return super.deserializeHeader(response, invokeContext);
    }

    @Override
    public <T extends ResponseCommand> boolean deserializeContent(T response, InvokeContext invokeContext) throws DeserializationException {
        if (response instanceof RpcResponseCommand) {
            RpcResponseCommand command = (RpcResponseCommand) response;
            Object o = command.getResponseObject();

            if (o instanceof Response) {
                Response r = (Response) o;
                byte sign = (byte) command.getResponseHeader();
                if (sign == RIGHT_RESPONSE) {

                }
                return true;
            }
        }
        return super.deserializeContent(response, invokeContext);
    }

    @Override
    public <T extends RequestCommand> boolean serializeHeader(T request, InvokeContext invokeContext) throws SerializationException {
        if (request instanceof RpcRequestCommand) {
            RpcRequestCommand command = (RpcRequestCommand) request;
            Object o = command.getRequestObject();
            if (o instanceof Request) {
                Request r = (Request) o;
                int cmd = r.getId();
                command.setHeader(serializer.encode(cmd));
            }
        }
        return super.serializeHeader(request, invokeContext);
    }

    @Override
    public <T extends ResponseCommand> boolean serializeContent(T response) throws SerializationException {
        if (response instanceof RpcResponseCommand) {
            RpcResponseCommand command = (RpcResponseCommand) response;
            Object o = command.getResponseObject();
            if (o instanceof Response) {
                Response r = (Response) o;
                Object value = r.getParam();
                command.setContent(serializer.encode(value));
                return true;
            }
        }
        return super.serializeContent(response);
    }

    @Override
    public <T extends RequestCommand> boolean serializeContent(T request, InvokeContext invokeContext) throws SerializationException {
        if (request instanceof RpcRequestCommand) {
            RpcRequestCommand command = (RpcRequestCommand) request;
            Object o = command.getRequestObject();
            if (o instanceof Request) {
                Object value = ((Request) o).getParam();
                byte[] content = serializer.encode(value);
                request.setContent(content);
                return true;
            }
        }
        return super.serializeContent(request, invokeContext);
    }

    @Override
    public <T extends RequestCommand> boolean deserializeHeader(T request) throws DeserializationException {
        if (request instanceof RpcRequestCommand) {
            RpcRequestCommand command = (RpcRequestCommand) request;
            byte[] header = command.getHeader();
            command.setRequestHeader(serializer.decode(header, Integer.class));
            return true;
        }
        return super.deserializeHeader(request);
    }

    @Override
    public <T extends RequestCommand> boolean deserializeContent(T request) throws DeserializationException {
        if (request instanceof RpcRequestCommand) {
            RpcRequestCommand command = (RpcRequestCommand) request;
            int cmd = (int) command.getRequestHeader();
            Request build = Request.of(cmd);
            Class<?> clazz = classManage.findClass(cmd);
            if (Objects.nonNull(clazz)) {
                byte[] data = command.getContent();
                Object value = serializer.decode(data, clazz);
                build.setParam(value);
            }
            command.setRequestObject(build);
            return true;
        }
        return super.deserializeContent(request);
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public void setClassManage(CmdClassManage classManage) {
        this.classManage = classManage;
    }
}
