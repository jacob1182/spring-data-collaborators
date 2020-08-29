package org.jasi.springdata.collaborators;

import org.jasi.springdata.collaborators.annotation.Collaborator;
import org.jasi.springdata.collaborators.domain.NotificationService;
import org.jasi.springdata.collaborators.domain.Order;
import org.jasi.springdata.collaborators.domain.proxy.CustomInvocationHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {CollaboratorApplication.class})
@RunWith(SpringRunner.class)
public class CollaboratorEntityInjectorTest {

    @Autowired
    private ApplicationContext context;

    private CollaboratorEntityInjector collaboratorEntityInjector;

    @Before
    public void setup() {
        collaboratorEntityInjector = new CollaboratorEntityInjector(context);
    }

    @Test
    public void shouldInjectCollaborators() {

        Order product = Order.of("Onion");

        collaboratorEntityInjector.injectCollaborators(product);

        assertThat(product.getNotificationService()).isNotNull();
        assertThat(product.getStoreInfo()).isNotNull();
    }


    @Test
    public void shouldInjectCollaboratorsWhenProxy() {

        Order product = Order.of("Onion");
        Order proxy = ProxyUtils.createProxy(product, true);

        collaboratorEntityInjector.injectCollaborators(proxy);

        assertThat(proxy.getNotificationService()).isNotNull();
        assertThat(proxy.getStoreInfo()).isNotNull();
    }

    @Test
    public void shouldInjectCollaboratorsWhenCustomProxy() {

        MyInterface target = new MyClass();
        MyInterface proxy = (MyInterface) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                MyClass.class.getInterfaces(),
                new CustomInvocationHandler(target)
        );

        collaboratorEntityInjector.injectCollaborators(proxy);

        assertThat(proxy.getNotificationService()).isNotNull();
    }

    interface MyInterface {
        NotificationService getNotificationService();
    }
    public static class MyClass implements MyInterface {

        @Collaborator
        private NotificationService notificationService;

        public NotificationService getNotificationService() {
            return notificationService;
        }
    }

}