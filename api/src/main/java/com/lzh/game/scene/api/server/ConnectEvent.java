package com.lzh.game.scene.api.server;

import com.lzh.game.scene.common.connect.Connect;

public class ConnectEvent {

    public static final int CONNECTED = 0;
    public static final int CLOSED = 1;

    private int type;

    private Connect connect;

    public Connect getConnect() {
        return connect;
    }

    public void setConnect(Connect connect) {
        this.connect = connect;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
