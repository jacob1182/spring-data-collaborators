package org.jasi.springdata.collaborators.domain.config;

import org.jasi.springdata.collaborators.annotation.CollaboratorConfig;
import org.jasi.springdata.collaborators.domain.providers.OrderThirdPartyEntityProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@CollaboratorConfig(providers = OrderThirdPartyEntityProvider.class)
public class OrderConfig {

    @Bean
    public OrderThirdPartyEntityProvider getOrderThirdPartyEntityProvider() {
        return new OrderThirdPartyEntityProvider();
    }
}
