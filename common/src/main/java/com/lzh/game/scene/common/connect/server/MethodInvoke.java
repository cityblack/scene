package com.lzh.game.scene.common.connect.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvoke implements Invoke {

    private Method method;

    private Object target;

    public MethodInvoke(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    @Override
    public Object invoke(Object... param) throws InvokeException {
        try {
            return method.invoke(target, param);
        } catch (IllegalAccessException e) {
            throw new InvokeException("Request invoke error!!", e);
        } catch (InvocationTargetException e) {
            throw new InvokeException("Request invoke error!!", e);
        }
    }
}
