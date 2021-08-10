package com.lzh.game.scene.common.connect;

import java.util.Collection;

public interface LoadBalance {

    Connect choose(Collection<Connect> connects, Request request);
}
