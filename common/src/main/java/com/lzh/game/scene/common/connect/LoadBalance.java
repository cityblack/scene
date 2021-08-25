package com.lzh.game.scene.common.connect;

import java.util.Collection;

public interface LoadBalance {

    <T extends Connect>T choose(Collection<T> connects, Request request);
}
