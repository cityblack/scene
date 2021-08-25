package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.LoadBalance;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.scene.SceneConnect;

import java.util.Collection;

public class ClientLoadBalance implements LoadBalance {

    @Override
    public <T extends Connect> T choose(Collection<T> connects, Request request) {
        return connects.iterator().next();
    }
}
