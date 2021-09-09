package com.lzh.game.scene.common.connect.sofa;

import com.alipay.remoting.DefaultCustomSerializer;
import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.exception.DeserializationException;
import com.alipay.remoting.exception.SerializationException;
import com.alipay.remoting.rpc.RequestCommand;
import com.alipay.remoting.rpc.ResponseCommand;
import com.alipay.remoting.rpc.RpcCommandType;
import com.alipay.remoting.rpc.protocol.RpcRequestCommand;
import com.alipay.remoting.rpc.protocol.RpcResponseCommand;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.common.utils.ClassUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.lzh.game.scene.common.ContextConstant.*;

/**
 * 节约带宽，按SOFA自身协议 把请求CMD和对应的class信息写到信息头里面
 */
public class SofaRpcSerializer extends DefaultCustomSerializer {

    private Serializer serializer;

    public SofaRpcSerializer() {
    }

    public SofaRpcSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public <T extends ResponseCommand> boolean serializeHeader(T response) throws SerializationException {
        if (response instanceof RpcResponseCommand) {
            RpcResponseCommand command = (RpcResponseCommand) response;
            Object o = command.getResponseObject();
            if (o instanceof Response) {
                Map<Byte, String> head = new HashMap<>(4);
                Response r = (Response) o;
                byte status = r.getStatus();
                head.put(EXCHANGE_RESPONSE_STATUS, String.valueOf(status));
                head.put(EXCHANGE_TYPE, r.getParamClassName());
                head.put(ERROR_RESPONSE_MSG_KEY, r.getError());
                command.setHeader(serializer.encode(head));
                return true;
            }
        }
        return super.serializeHeader(response);
    }

    @Override
    public <T extends ResponseCommand> boolean deserializeHeader(T response, InvokeContext invokeContext) throws DeserializationException {
        if (response instanceof RpcResponseCommand) {
            RpcResponseCommand command = (RpcResponseCommand) response;
            Map<Byte, String> head = serializer.decode(command.getHeader(), HashMap.class);
            command.setResponseHeader(head);
            return true;
        }
        return super.deserializeHeader(response, invokeContext);
    }

    @Override
    public <T extends ResponseCommand> boolean deserializeContent(T response, InvokeContext invokeContext) throws DeserializationException {
        if (response instanceof RpcResponseCommand) {
            RpcResponseCommand command = (RpcResponseCommand) response;
            Map<Byte, String> head = (Map<Byte, String>) command.getResponseHeader();
            Response resp = Response.of();
            byte status = Byte.parseByte(head.get(EXCHANGE_RESPONSE_STATUS));
            String msg = head.get(ERROR_RESPONSE_MSG_KEY);
            resp.setStatus(status);
            resp.setError(msg);
            command.setResponseObject(resp);

            byte[] content = command.getContent();
            if (Objects.isNull(content) || content.length <= 0) {
                return true;
            }
            if (status != RIGHT_RESPONSE) {
                return true;
            }
            String typeKey = head.get(EXCHANGE_TYPE);
            Class<?> type = getParamClass(typeKey);
            resp.setParam(serializer.decode(command.getContent(), type));
            resp.setParamClass(type);
            resp.setParamClassName(typeKey);
            return true;
        }
        return super.deserializeContent(response, invokeContext);
    }

    @Override
    public <T extends ResponseCommand> boolean serializeContent(T response) throws SerializationException {
        if (response instanceof RpcResponseCommand) {
            RpcResponseCommand command = (RpcResponseCommand) response;
            Object o = command.getResponseObject();
            if (o instanceof Response) {
                Response<?> r = (Response<?>) o;
                Object value = r.getParam();
                if (Objects.nonNull(value)) {
                    command.setContent(serializer.encode(value));
                }
                return true;
            }
        }
        return super.serializeContent(response);
    }

    @Override
    public <T extends RequestCommand> boolean serializeHeader(T request, InvokeContext invokeContext) throws SerializationException {
        if (request instanceof RpcRequestCommand) {
            RpcRequestCommand command = (RpcRequestCommand) request;
            Object o = command.getRequestObject();
            if (o instanceof Request) {
                Request r = (Request) o;
                Map<Byte, String> header = new HashMap<>(4);
                header.put(EXCHANGE_CMD, String.valueOf(r.getId()));
                if (Objects.nonNull(r.getParam())) {
                    header.put(EXCHANGE_TYPE, r.getParamClassName());
                }
                command.setHeader(serializer.encode(header));
            }
        }
        return super.serializeHeader(request, invokeContext);
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
            Map<Byte, String> head = serializer.decode(header, HashMap.class);
            command.setRequestHeader(head);
            return true;
        }
        return super.deserializeHeader(request);
    }

    @Override
    public <T extends RequestCommand> boolean deserializeContent(T request) throws DeserializationException {
        if (request instanceof RpcRequestCommand) {
            RpcRequestCommand command = (RpcRequestCommand) request;
            Map<Byte, String> head = (Map<Byte, String>) command.getRequestHeader();
            int cmd = Integer.parseInt(head.get(EXCHANGE_CMD));
            String typeKey = head.get(EXCHANGE_TYPE);
            Request build = Request.of(cmd);
            build.setType(request.getType());
            build.setParamClassName(typeKey);
            build.setParamClass(getParamClass(typeKey));
            build.setOneWay(command.getType() == RpcCommandType.REQUEST_ONEWAY);

            Class<?> clazz = build.getParamClass();
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

    private Class<?> getParamClass(String key) {
        if (Objects.isNull(key)) {
            return null;
        }
        return ClassUtils.getClassByName(key);
    }
}
