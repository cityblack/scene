package com.lzh.game.scene.common.connect.server;

import com.lzh.game.scene.common.connect.server.cmd.Action;
import com.lzh.game.scene.common.connect.server.cmd.Cmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SimpleInvokeFactory implements MethodInvokeFactory {

    private static final Logger logger = LoggerFactory.getLogger(SimpleInvokeFactory.class);

    private RequestHelper helper;

    private Collection<Object> actionsBean;

    public SimpleInvokeFactory(RequestHelper helper, Collection<Object> actionsBean) {
        this.helper = helper;
        this.actionsBean = actionsBean;
    }

    @Override
    public void loadMethodInvoke(InvokeManage manage) {

        synchronized (SimpleInvokeFactory.class) {
            final Set<Class<?>> innerParam = getInnerParamType(helper);
            actionsBean.forEach((v) -> {
                Class<?> clazz = v.getClass();
                Action action = clazz.getAnnotation(Action.class);
                if (Objects.isNull(action)) {
                    throw new IllegalArgumentException("Target bean is not @action");
                }
                int value = action.value();
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method: methods) {
                    Cmd cmd = method.getAnnotation(Cmd.class);
                    int cmdValue = value + cmd.value();
                    int index = parseMethodRequestParamIndex(innerParam, method);
                    MethodInvoke invoke = new MethodInvokeEndpoint(v, method, index);
                    manage.registerInvoke(cmdValue, invoke);
                    logger.info("Loaded {} cmd method!!", cmdValue);
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

    private static Set<Class<?>> getInnerParamType(RequestHelper helper) {
        Set<Class<?>> set = new HashSet<>(8);
        for (Class<?> type : helper.innerParam()) {
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
