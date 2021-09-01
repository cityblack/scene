package com.lzh.game.scene.common;

import java.io.Serializable;
import java.util.Map;

public class SceneInstance implements Serializable {

    private static final long serialVersionUID = -1904095131138925745L;

    private String group;

    private String unique;

    private int map;

    // 额外信息
    private Map<String, String> extInfo;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    @Override
    public String toString() {
        return "SceneInstance{" +
                "group='" + group + '\'' +
                ", unique='" + unique + '\'' +
                ", map=" + map +
                '}';
    }
}
