package org.jasi.springdata.collaborators;

import org.jasi.springdata.collaborators.domain.Order;
import org.jasi.springdata.collaborators.domain.OrderItem;
import org.jasi.springdata.collaborators.domain.OrderItemFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = CollaboratorTestConfig.class)
@RunWith(SpringRunner.class)
public class EntityFactoryRegistryTest {

    @Autowired
    private ApplicationContext context;

    private EntityFactoryRegistry registry;

    @Before
    public void setUp() {
        registry = new EntityFactoryRegistry(context);
    }

    @Test
    public void shouldRegisterAllFactories() {

        assertThat(registry.factoryBy(Order.class)).isNotPresent();
        assertThat(registry.factoryBy(OrderItem.class))
                .isPresent()
                .get()
                .isInstanceOf(OrderItemFactory.class);
    }
}