package com.lzh.game.scene.api.connect;

import com.lzh.game.scene.api.client.Request;
import com.lzh.game.scene.api.client.Response;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.common.NodeType;

public interface Connect {

    Response sendMessage(Request request);

    String key();

    Member member();

    int ReflectCount();

    NodeType nodeType();
}
