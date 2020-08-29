package org.jasi.springdata.collaborators.injectors;

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
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ContextConfiguration(classes = CollaboratorTestConfig.class)
@RunWith(SpringRunner.class)
public class CollaboratorInjectorHandlerTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderFileReader orderFileReader;

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

    private final Consumer<Order> hasCollaborators = order -> {
        assertThat(order.getNotificationService()).isNotNull();
        assertThat(order.getStoreInfo()).isNotNull()
                .isInstanceOf(JsonStoreInfoService.class);
    };

    @Test
    public void shouldAddCollaboratorWhenRetrievingOrderArray() {
        assertThat(orderFileReader.readAll())
                .allSatisfy(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenRetrievingOptionalOrder() {
        Example<Order> query = Example.of(Order.of("Onion"));
        assertThat(orderRepository.findOne(query))
                .isPresent()
                .get()
                .satisfies(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenRetrievingOrderList() {
        assertThat(orderRepository.findAll())
                .allSatisfy(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenRetrievingOrderStream() {
        assertThat(orderRepository.findAllBy())
                .allSatisfy(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenRetrievingOrderPage() {
        assertThat(orderRepository.findAll(Pageable.unpaged()))
                .allSatisfy(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenRetrievingProxyObject() {
        assertThat(orderRepository.findProjectionByName("Onion"))
                .isNotNull()
                .satisfies(orderProjection -> assertThat(orderProjection.getNotificationService()).isNotNull());
    }

}