package com.lzh.game.scene.api.connect;

import java.util.Collection;

public interface LoadBalance {

    Connect choose(Collection<Connect> connects);
}
