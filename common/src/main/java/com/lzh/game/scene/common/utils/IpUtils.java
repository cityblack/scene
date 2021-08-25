package com.lzh.game.scene.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {

    public static String localIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new NullPointerException("Can't get local host");
        }
    }
}
