package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.LoadBalance;
import com.lzh.game.scene.common.connect.Request;

import java.util.Collection;

public class ClientLoadBalance implements LoadBalance {

    @Override
    public Connect choose(Collection<Connect> connects, Request request) {
        return connects.iterator().next();
    }
}
