package com.lzh.game.scene.common.connect.server;

/**
 * 默认method的包装, 默认可以内置一些参数, 但请求参数最多只能一个(因为使用Protobuff, 再封装一层太麻烦，不如做约定)
 * 如: request(Connect connect, int param) is right
 *    request(int param, String param) is error
 */
public interface MethodInvoke {

    Object invoke(Object... param) throws InvokeException;

    Class<?>[] params();
}
