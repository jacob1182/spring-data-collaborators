package org.jasi.springdata.collaborators;

import org.aopalliance.intercept.MethodInterceptor;
import org.jasi.springdata.collaborators.domain.Order;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jasi.springdata.collaborators.ProxyUtils.createProxy;
import static org.jasi.springdata.collaborators.ProxyUtils.getProxyTarget;

public class ProxyUtilsTest {

    public interface ProxyInterface {}
    public static class ProxyObject implements ProxyInterface {}

    @Test
    public void shouldCreateProxyWithAdvice() {
        Order target = Order.of("Onion");
        MethodInterceptor advice = invocation -> invocation.getMethod().invoke(target, invocation.getArguments());

        Order proxy = createProxy(target, true, advice);

        assertThat(proxy.getName()).isEqualTo(target.getName());
    }

    @Test
    public void shouldGetSameTargetWhenNoProxy() {
        Order target = Order.of("Onion");

        Order proxyTarget = getProxyTarget(target);

        assertThat(proxyTarget).isSameAs(target);
    }

    @Test
    public void shouldGetTheProxyTargetWhenTargetClassProxy() {
        Order target = Order.of("Onion");
        Order proxy = createProxy(target, true);

        Order proxyTarget = getProxyTarget(proxy);

        assertThat(proxyTarget).isEqualTo(target);
    }

    @Test
    public void shouldGetTheProxyTargetWhenTargetInterfaceProxy() {
        ProxyInterface target = new ProxyObject();
        ProxyInterface proxy = createProxy(target, false);

        ProxyInterface proxyTarget = getProxyTarget(proxy);

        assertThat(proxyTarget).isEqualTo(target);
    }


    @Test
    public void shouldGetTheSameTargetWhenUnknownProxy() {
        ProxyInterface target = new ProxyObject();
        ProxyInterface proxy = (ProxyInterface) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                ((proxy1, method, args) -> method.invoke(target, args))
        );

        ProxyInterface proxyTarget = getProxyTarget(proxy);

        assertThat(proxyTarget).isSameAs(proxy);
    }
}