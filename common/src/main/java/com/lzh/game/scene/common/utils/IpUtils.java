package com.lzh.game.scene.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class IpUtils {

    public static String localIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new NullPointerException("Can't get local host");
        }
    }

    public static boolean isLocalIp(String ip) {
        return Objects.equals(ip, "127.0.0.1")
                || Objects.equals(ip, "0.0.0.0")
                || Objects.equals(ip, localIp());
    }
}
