package org.jasi.springdata.collaborators.providers;

import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

public class EntityProviderMatcher {

    private final Class<?>[] providers;

    public EntityProviderMatcher(Class<?>[] providers) {
        this.providers = providers;
    }

    public boolean matches(Object bean) {
        return isThirdPartyEntityProvider(bean) || isEntityProvider(bean) || isRepository(bean);
    }

    private boolean isRepository(Object bean) {
        return bean instanceof Repository
                || bean.getClass()
                .isAnnotationPresent(org.springframework.stereotype.Repository.class);
    }

    private boolean isEntityProvider(Object bean) {
        return bean instanceof EntityProvider
                || bean.getClass()
                .isAnnotationPresent(org.jasi.springdata.collaborators.annotation.EntityProvider.class);
    }

    private boolean isThirdPartyEntityProvider(Object bean) {
        return Stream.of(providers).anyMatch(type -> type.isInstance(bean));
    }
}
