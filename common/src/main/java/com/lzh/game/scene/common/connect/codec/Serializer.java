package com.lzh.game.scene.common.connect.codec;

import java.util.Map;

public interface Serializer {

    byte[] encode(Object o);

    <T>T decode(byte[] data, Class<T> clazz);

    <K, V>byte[] encodeMap(Map<K, V> o);

    <K, V>Map<K, V> decodeMap(byte[] data);
}
