package org.jasi.springdata.collaborators.providers;

import org.jasi.springdata.collaborators.CollaboratorTestConfig;
import org.jasi.springdata.collaborators.domain.JsonStoreInfoService;
import org.jasi.springdata.collaborators.domain.Order;
import org.jasi.springdata.collaborators.domain.OrderFileReader;
import org.jasi.springdata.collaborators.domain.OrderRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = CollaboratorTestConfig.class)
@RunWith(SpringRunner.class)
public class EntityProviderBeanPostProcessorTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderFileReader orderFileReader;

    private final Consumer<Order> hasCollaborators = order -> {
        assertThat(order.getNotificationService()).isNotNull();
        assertThat(order.getStoreInfo()).isNotNull()
                .isInstanceOf(JsonStoreInfoService.class);
    };

    @Before
    public void setUp() {
        orderRepository.saveAll(asList(
                Order.of("Bag"),
                Order.of("Onion")
        ));
    }

    @After
    public void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    public void shouldAddCollaboratorWhenRetrievingOrder() {
        assertThat(orderRepository.findByName("Cell-Phone")).isNull();
        assertThat(orderRepository.findByName("Onion"))
                .isNotNull()
                .satisfies(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenUsingEntityProvider() {
        assertThat(orderFileReader.readNext().getData())
            .satisfies(hasCollaborators);
    }
}
