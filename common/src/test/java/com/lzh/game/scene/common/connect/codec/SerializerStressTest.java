package com.lzh.game.scene.common.connect.codec;

import org.openjdk.jmh.annotations.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 将protostuff 序列化集合需要包裹一层，费带宽. 如果遍历集合序序列化性能则差2倍左右
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 5, time = 20)
public class SerializerStressTest {

    private Map<Byte, String> data;

    private Serializer serializer;

    @Setup
    public void init() {
        Map<Byte, String> map = new HashMap<>();
        map.put((byte) 0x00, "hello world");
        map.put((byte) 0x01, "hello world2");
        map.put((byte) 0x02, "hello world3");
        map.put((byte) 0x03, "hello world4");
        this.data = map;
        this.serializer = new ProtostuffSerializer();
    }

    @Benchmark
    public void wrapperMap() {
        Wrapper wrapper = new Wrapper();
        wrapper.setMap(this.data);
        serializer.encode(wrapper);
    }

    @Benchmark
    public void consumerMap() {
        try {
            encodeMap(this.data);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
