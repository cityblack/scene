package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.LoadBalance;

import java.util.Collection;

public class ClientLoadBalance implements LoadBalance {

    @Override
    public Connect choose(Collection<Connect> connects) {
        return connects.iterator().next();
    }
}
