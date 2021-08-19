package com.lzh.game.scene.common.connect.codec;

public interface Serializer {

    byte[] encode(Object o);

    <T>T decode(byte[] data, Class<T> clazz);
}
