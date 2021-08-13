package com.lzh.game.scene.common.connect.codec;

import java.util.Map;

public class Wrapper<T, V> {
    private Map<T, V> map;

    public Map<T, V> getMap() {
        return map;
    }

    public void setMap(Map<T, V> map) {
        this.map = map;
    }
}
