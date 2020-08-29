package org.jasi.springdata.collaborators.domain.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CustomInvocationHandler implements InvocationHandler {

    private final Object target;

    public CustomInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return target.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(target, args);
    }

    public Object getTarget() {
        return target;
    }
}
