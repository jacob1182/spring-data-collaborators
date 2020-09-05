package org.jasi.springdata.collaborators.domain.providers;

import org.jasi.springdata.collaborators.domain.Order;
import org.jasi.springdata.collaborators.domain.wrappers.Resource;
import org.jasi.springdata.collaborators.providers.EntityProvider;
import org.springframework.stereotype.Component;

@Component
public class OrderFileReader implements EntityProvider {

    public Resource<Order> readNext() {
        return Resource.of(Order.of("From file"), "http://self");
    }

    public Order[] readAll() {
        return new Order[] {
                Order.of("Bag"),
                Order.of("Onion")
        };
    }
}
