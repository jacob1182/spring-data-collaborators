package org.jasi.springdata.collaborators.domain.proxy;

import org.jasi.springdata.collaborators.extractor.ProxyTargetExtractor;
import org.springframework.stereotype.Component;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.isProxyClass;

@Component
public class CustomProxyTargetExtractor implements ProxyTargetExtractor  {

    @Override
    public boolean support(Object proxy) {
        return isProxyClass(proxy.getClass()) && getInvocationHandler(proxy) instanceof CustomInvocationHandler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object extractTargetFrom(Object proxy) {
        CustomInvocationHandler invocationHandler = (CustomInvocationHandler) getInvocationHandler(proxy);
        return invocationHandler.getTarget();
    }
}
