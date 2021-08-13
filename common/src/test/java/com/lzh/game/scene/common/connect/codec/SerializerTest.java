package com.lzh.game.scene.common.connect.codec;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SerializerTest {

    private Serializer serializer = new ProtostuffSerializer();


    @Test
    void serializeMap() throws IOException {
        Map<Byte, String> map = new HashMap<>();
        map.put((byte) 0x01, "1");
        map.put((byte) 0x02, "2");
        map.put((byte) 0x03, "2");
        byte[] bytes = serializer.encodeMap(map);
        System.out.println(bytes.length);
        Map<Byte, String> deMap = serializer.decodeMap(bytes);
        System.out.println(deMap);
        /*Wrapper<Byte, String> wrapper = new Wrapper();
        Map<Byte, String> map = new HashMap<>();
        map.put((byte) 0x01, "1");
        map.put((byte) 0x02, "2");
        map.put((byte) 0x03, "2");
        wrapper.setMap(map);
        byte[] bytes = serializer.encode(wrapper);
        System.out.println(bytes.length);
        System.out.println(encodeMap(map).length);
        Wrapper decode = serializer.decode(bytes, Wrapper.class);
        System.out.println(decode.getMap());*/
    }

    private byte[] encodeMap(Map<Byte, String> map) throws IOException {
        // 1024 避免扩容影响
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        for (Map.Entry<Byte, String> entry : map.entrySet()) {
            byte[] key = new byte[]{ entry.getKey() };
            byte[] v = entry.getValue().getBytes();

            outputStream.write(key.length);
            outputStream.write(key);
            outputStream.write(v.length);
            outputStream.write(v);
        }

        byte[] bytes = outputStream.toByteArray();
        outputStream.close();
        return bytes;
    }

    @Test
    void encodeBase() {
        int i = 100-000-000;
        byte[] bytes = serializer.encode(i);
        System.out.println(bytes.length);
    }

    @Test
    void encodeList() throws IOException {
        List<String> list = Arrays.asList("lzh");
        byte[] bytes = serializer.encode(list);
        System.out.println(bytes.length);
        System.out.println(list.getClass().getName());
        List<String> s = serializer.decode(bytes, list.getClass());
        System.out.println(s);
    }

    @Test
    void encode() {
        A a = new B(28);
        a.name = "lzh";
        byte[] data = serializer.encode(a);
        System.out.println(data.length);
    }

    @Test
    void decode() {
        A a = new B(28);
        a.name = "lzh";
        byte[] data = serializer.encode(a);

        B b = serializer.decode(data, B.class);
        System.out.println(b);
    }

    public abstract class A {
        public String name;
    }

    public class B extends A {
        public int age;

        public B(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "B{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
