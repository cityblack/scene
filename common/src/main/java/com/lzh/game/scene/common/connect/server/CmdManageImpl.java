package com.lzh.game.scene.common.connect.server;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

public class CmdManageImpl implements CmdManage {

    private Map<Integer, CmdModel> relationMap = new HashMap<>();


    @Override
    public boolean put(int cmd, CmdModel cmdModel) {
        return false;
    }

    @Override
    public boolean contain(int cmd) {
        return false;
    }

    @Override
    public Class<?> getClass(int cmd) {
        return null;
    }

    @Override
    public Model get(int cmd) {
        return null;
    }

    @Override
    public Invoke getInvoke(int cmd) {
        return null;
    }
}
