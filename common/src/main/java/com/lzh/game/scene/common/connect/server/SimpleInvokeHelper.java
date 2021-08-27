package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.server.cmd.Action;
import com.lzh.game.scene.common.connect.server.cmd.Cmd;
import com.lzh.game.scene.common.connect.server.convert.AbstractRequestParamConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SimpleInvokeHelper implements MethodInvokeHelper {

    private static final Logger logger = LoggerFactory.getLogger(SimpleInvokeHelper.class);

    @Override
    public void addMethodInvoke(InvokeManage manage, List<Object> targets) {
        synchronized (SimpleInvokeHelper.class) {
            final Set<Class<?>> innerParam = getInnerParamType();
            targets.forEach((v) -> {
                Class<?> clazz = v.getClass();
                Action action = clazz.getAnnotation(Action.class);
                if (Objects.isNull(action)) {
                    throw new IllegalArgumentException("Target " + clazz.getName() + " bean is not @Action");
                }
                int value = action.value();
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method: methods) {
                    Cmd cmd = method.getAnnotation(Cmd.class);
                    if (Objects.isNull(cmd)) {
                        continue;
                    }
                    int cmdValue = value + cmd.value();
                    int index = parseMethodRequestParamIndex(innerParam, method);
                    MethodInvoke invoke = new MethodInvokeEndpoint(v, method, index);
                    manage.registerInvoke(cmdValue, invoke);
                    logger.info("Loaded {} cmd mapping {}#{}", cmdValue, clazz.getName(), method.getName());
                }
            });
        }
    }

    private static int parseMethodRequestParamIndex(Set<Class<?>> innerParam, Method method) {
        Class<?>[] params = method.getParameterTypes();
        int index = -1;
        for (int i = 0; i < params.length; i++) {
            Class<?> param = params[i];
            if (innerParam.contains(param)) {
                continue;
            }
            if (index >= 0) {
                throw new IllegalArgumentException("The request method param not unique" +
                        ". please check the method:" + method.getName());
            }
            index = i;
        }

        return index;
    }

    private static Set<Class<?>> getInnerParamType() {
        Set<Class<?>> set = new HashSet<>(8);
        for (Class<?> type : AbstractRequestParamConvert.getInnerParamTypes()) {
            set.add(type);
        }
        return set;
    }

    public class MethodInvokeEndpoint implements MethodInvoke {

        private Object target;

        private Method method;

        private int extParamIndex;

        public MethodInvokeEndpoint(Object target, Method method, int extParamIndex) {
            this.target = target;
            this.method = method;
            this.extParamIndex = extParamIndex;
        }

        public Object getTarget() {
            return target;
        }

        public void setTarget(Object target) {
            this.target = target;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        @Override
        public Object invoke(Object... param) throws InvocationTargetException, IllegalAccessException {
            return this.method.invoke(this.target, param);
        }

        @Override
        public Class<?>[] params() {
            return method.getParameterTypes();
        }

        @Override
        public int extParamIndex() {
            return extParamIndex;
        }
    }
}
