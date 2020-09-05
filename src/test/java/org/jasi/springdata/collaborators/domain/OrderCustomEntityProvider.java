package org.jasi.springdata.collaborators.domain;

import org.jasi.springdata.collaborators.annotation.EntityProvider;
import org.springframework.stereotype.Component;

@Component
@EntityProvider
public class OrderCustomEntityProvider {

    public Order getOrder() {
        return Order.of("Onion");
    }
}
