package com.lzh.game.scene.api;

import java.io.Serializable;

public class ResponseBody implements Serializable {

    private int id;

    private String msg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
