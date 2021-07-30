package com.lzh.game.scene.common.connect.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvokeEndpoint implements MethodInvoke {

    private static final Logger logger = LoggerFactory.getLogger(MethodInvokeEndpoint.class);

    private Object target;

    private Method method;

    private Class<?>[] params;

    public MethodInvokeEndpoint(Object target, Method method, Class<?>[] params) {
        this.target = target;
        this.method = method;
        this.params = params;
    }

    @Override
    public Object invoke(Object... param) throws InvokeException {
        try {
            return method.invoke(target, param);
        } catch (IllegalAccessException e) {
            logger.error("", e);
            throw new InvokeException("Request error", e);
        } catch (InvocationTargetException e) {
            logger.error("", e);
            throw new InvokeException("", e);
        }
    }

    @Override
    public Class<?>[] params() {
        return params;
    }
}
