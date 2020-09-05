package org.jasi.springdata.collaborators.domain.providers;

import org.jasi.springdata.collaborators.domain.Order;

public class OrderThirdPartyEntityProvider {
    public Order getOrder() {
        return Order.of("Onion");
    }
}
