package com.lzh.game.scene.common.connect.codec;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProtostuffSerializer implements Serializer {

    @Override
    public byte[] encode(Object object) {
        if (Objects.isNull(object)) {
            return new byte[0];
        }
        Class<?> clazz = object.getClass();
        if (isWrapperType(clazz)) {
            Wrapper wrapper = new Wrapper();
            wrapper.data = object;
            return encode(wrapper);
        }
        Schema schema = RuntimeSchema.getSchema(clazz);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return ProtostuffIOUtil.toByteArray(object, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T decode(byte[] data, Class<T> clazz) {
        if (isWrapperType(clazz)) {
            return (T) decode(data, Wrapper.class).data;
        }
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T message = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, message, schema);
        return message;
    }

    class Wrapper<T> {
        T data;
    }

    private boolean isWrapperType(Class<?> clazz) {
        return clazz.isArray() || Map.class.isAssignableFrom(clazz)
                || Collection.class.isAssignableFrom(clazz);
    }
}
