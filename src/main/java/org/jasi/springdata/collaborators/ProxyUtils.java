package org.jasi.springdata.collaborators;

import org.aopalliance.aop.Advice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.util.stream.Stream;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.isProxyClass;

public class ProxyUtils {

    private static final Log logger = LogFactory.getLog(ProxyUtils.class);

    /**
     * Creates a Cglib (class-based) proxy if target is not a proxy already.
     *
     * @param target the target object
     * @param advice array of advices
     * @return the proxy
     * */
    @SuppressWarnings("unchecked")
    public static <T, R extends T> R createProxy(T target, boolean targetClass, Advice... advice) {

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(targetClass);
        Stream.of(advice).forEach(proxyFactory::addAdvice);
        Stream.of(target.getClass().getInterfaces())
                .forEach(proxyFactory::addInterface);

        return (R) proxyFactory.getProxy();
    }

    /**
     * Extract the target object when given a proxy otherwise returns the same object.
     *
     * @param proxy the proxy object
     * @return the target object. If proxy is not a valid proxy then returns proxy.
     * */
    @SuppressWarnings("unchecked")
    static <T, R extends T> R getProxyTarget(T proxy) {

        if (!(proxy instanceof SpringProxy) && !isProxyClass(proxy.getClass()))
            return (R) proxy;

        R target = (R) AopProxyUtils.getSingletonTarget(proxy);

        if (target != null)
            return target;

        try {
            InvocationHandler invocationHandler = getInvocationHandler(proxy);
            if (invocationHandler instanceof org.springframework.aop.framework.AopProxy) {
                Field advisedField = ReflectionUtils.findField(invocationHandler.getClass(),"advised");

                boolean accessibility = advisedField.isAccessible();
                ReflectionUtils.makeAccessible(advisedField);
                AdvisedSupport advised = (AdvisedSupport) advisedField.get(invocationHandler);
                advisedField.setAccessible(accessibility);

                return (R) advised.getTargetSource().getTarget();
            }
        } catch (Exception  e) {
            // no action
            logger.error("Accessing the proxy source: " + e.getMessage());
        }

        return (R) proxy;
    }
}
