package org.jasi.springdata.collaborators.domain.providers;

import org.jasi.springdata.collaborators.annotation.EntityProvider;
import org.jasi.springdata.collaborators.domain.Order;
import org.springframework.stereotype.Component;

@Component
@EntityProvider
public class OrderCustomEntityProvider {

    public Order getOrder() {
        return Order.of("Onion");
    }
}
