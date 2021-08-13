package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.BootstrapConfig;

public class ServerConfig implements BootstrapConfig {

    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void check() {

    }
}
