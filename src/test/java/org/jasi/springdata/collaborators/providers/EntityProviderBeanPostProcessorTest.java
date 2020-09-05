package org.jasi.springdata.collaborators.providers;

import org.jasi.springdata.collaborators.CollaboratorTestConfig;
import org.jasi.springdata.collaborators.domain.JsonStoreInfoService;
import org.jasi.springdata.collaborators.domain.Order;
import org.jasi.springdata.collaborators.domain.OrderRepository;
import org.jasi.springdata.collaborators.domain.providers.OrderCustomEntityProvider;
import org.jasi.springdata.collaborators.domain.providers.OrderFileReader;
import org.jasi.springdata.collaborators.domain.providers.OrderThirdPartyEntityProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private OrderCustomEntityProvider orderCustomEntityProvider;

    @Autowired
    private OrderThirdPartyEntityProvider orderThirdPartyEntityProvider;

    @Autowired
    private RestTemplate restTemplate;

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
    public void shouldAddCollaboratorWhenUsingEntityProviderInterface() {
        assertThat(orderFileReader.readAll())
                .allSatisfy(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenUsingEntityProviderInterfaceAndWrappers() {
        assertThat(orderFileReader.readNext().getData())
                .satisfies(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenUsingEntityProviderAnnotation() {
        assertThat(orderCustomEntityProvider.getOrder())
                .satisfies(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenUsingEntityProviderFromThirdParty() {
        assertThat(orderThirdPartyEntityProvider.getOrder())
                .satisfies(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenUsingRestTemplate() {
        assertThat(restTemplate.getForObject("http://invalid.server", Order.class))
                .satisfies(hasCollaborators);
    }

    @Test
    public void shouldAddCollaboratorWhenUsingRestTemplateWithWrapper() {
        assertThat(restTemplate.getForEntity("http://invalid.server", Order.class).getBody())
                .satisfies(hasCollaborators);
    }
}
