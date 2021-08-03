package com.lzh.game.scene.core.exchange;

/**
 * 内部交互一致性请求协议
 * 拆出来是为了与业务线程做区分
 */
public class DataRequest {

    /**
     * 命令标识
     * {@link InnerCmd}
     */
    private int cmd;

    private byte[] data;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
