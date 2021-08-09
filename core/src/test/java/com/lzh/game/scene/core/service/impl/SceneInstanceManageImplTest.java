package com.lzh.game.scene.core.service.impl;

import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.core.service.SceneInstanceManage;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 5, time = 30)
public class SceneInstanceManageImplTest {

    @State(Scope.Group)
    public static class PS {
        SceneInstanceManage manage;
        AtomicInteger count = new AtomicInteger(0);
    }

    private String group ="group";

    @Setup
    public void init(final PS ps) {
        ps.manage = new SceneInstanceManageImpl();
    }

    @Benchmark
    @Group("readWrite")
    @GroupThreads(2)
    public void get(final PS ps) {
        ps.manage.get(group);
    }

    @Benchmark
    @Group("readWrite")
    @GroupThreads(2)
    public void get2(final PS ps) {
        ps.manage.get("2");
    }

    @Benchmark
    @Group("readWrite")
    @GroupThreads(2)
    public void get3(final PS ps) {
        ps.manage.get("3");
    }

    @Benchmark
    @Group("readWrite")
    @GroupThreads(2)
    public void put(final PS ps) {
        SceneInstance instance = new SceneInstance();
//        int count = ps.count.incrementAndGet();
        instance.setMap(1);
        instance.setUnique("1");
        instance.setGroup(group);
        ps.manage.put(group, instance);
    }

    @Benchmark
    @Group("readWrite")
    @GroupThreads(2)
    public void put2(final PS ps) {
        SceneInstance instance = new SceneInstance();
//        int count = ps.count.incrementAndGet();
        instance.setMap(1);
        instance.setUnique("2");
        instance.setGroup("2");
        ps.manage.put("2", instance);
    }

    @Benchmark
    @Group("readWrite")
    @GroupThreads(2)
    public void put3(final PS ps) {
        SceneInstance instance = new SceneInstance();
//        int count = ps.count.incrementAndGet();
        instance.setMap(1);
        instance.setUnique("3");
        instance.setGroup("3");
        ps.manage.put("3", instance);
    }
}
