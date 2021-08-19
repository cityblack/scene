package com.lzh.game.scene.common;

public class ContextConstant {

    // sofa connect标识包装的connect 通过该标识从connect找到sofa
    public static String SOURCE_CONNECT_RELATION = "connect.id.source.relation";

    public static String SOFA_ASYNC_CONTEXT = "async.context";

    public static String SOFA_CONNECT_REQUEST = "connect.context";

    public static final String REDIS_MAP_KEY_PRE = "_map_key";

    // 正确状态响应码
    public static final byte RIGHT_RESPONSE = 0x0;

    public static final byte ERROR_COMMON_RESPONSE = 0x1;
    // ====== client - server 交互状态标识位 ==============
    public static final Byte EXCHANGE_CMD = 0x00;
    // 请求/应答参数类型
    public static final Byte EXCHANGE_TYPE = 0x01;
    // 应答标识
    public static final Byte EXCHANGE_RESPONSE_STATUS = 0x02;
    // 应答错误信息标识
    public static final Byte ERROR_RESPONSE_MSG_KEY = 0x03;
    // ======= end =============

    public static final int ALL_MAP_LISTEN_KEY = -1;
}
