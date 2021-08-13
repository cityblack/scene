package com.lzh.game.scene.common.connect.codec;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;

public class ProtostuffSerializer implements Serializer {

    @Override
    public byte[] encode(Object object) {
        Class<?> clazz = object.getClass();
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
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T message = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, message, schema);
        return message;
    }

    @Override
    public <K, V> byte[] encodeMap(Map<K, V> o) {
        Wrapper<K, V> wrapper = new Wrapper<>();
        wrapper.data = o;
        return this.encode(wrapper);
    }

    @Override
    public <K, V> Map<K, V> decodeMap(byte[] data) {
        Wrapper<K, V> wrapper = this.decode(data, Wrapper.class);
        return wrapper.data;
    }

    class Wrapper<K, V> {
        Map<K, V> data;
    }
}
