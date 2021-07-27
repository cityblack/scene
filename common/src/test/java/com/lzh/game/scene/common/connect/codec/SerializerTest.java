package com.lzh.game.scene.common.connect.codec;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class SerializerTest {

    private Serializer serializer = new ProtostuffSerializer();

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
