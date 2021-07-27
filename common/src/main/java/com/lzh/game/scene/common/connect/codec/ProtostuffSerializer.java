package com.lzh.game.scene.common.connect.codec;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffSerializer implements Serializer {

    @Override
    public byte[] encode(Object object) {
        Class<?> clazz = object.getClass();
        Schema schema = RuntimeSchema.getSchema(clazz);
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
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
}
