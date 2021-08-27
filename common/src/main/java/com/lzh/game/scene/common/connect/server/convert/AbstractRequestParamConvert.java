package com.lzh.game.scene.common.connect.server.convert;

import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequestParamConvert implements RequestParamConvert {

    public static final Logger logger = LoggerFactory.getLogger(AbstractRequestParamConvert.class);

    private static Map<Class<?>, RequestParamConvert> convertMap = new HashMap<>();

    public static void registerConvert(Class<?> paramType, RequestParamConvert convert) {
        convertMap.put(paramType, convert);
        logger.info("Register {} request param convert", paramType.getName());
    }

    public static Collection<Class<?>> getInnerParamTypes() {
        return convertMap.keySet();
    }

    public static RequestParamConvert getConvert(Class<?> paramType) {
        return convertMap.get(paramType);
    }

    @Override
    public Object convert(Request request) {
        return doConvert(request);
    }

    public abstract Object doConvert(Request request);

    static {
        AbstractRequestParamConvert.registerConvert(Connect.class, new ConnectConvert());
        AbstractRequestParamConvert.registerConvert(Request.class, new RequestConvert());
        AbstractRequestParamConvert.registerConvert(Response.class, new ResponseConvert());
        AbstractRequestParamConvert.registerConvert(SceneConnect.class, new SceneConnectConvert());
    }
}
