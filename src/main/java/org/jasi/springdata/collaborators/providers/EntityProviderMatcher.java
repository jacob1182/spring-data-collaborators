package org.jasi.springdata.collaborators.providers;

import org.springframework.data.repository.Repository;

public class EntityProviderMatcher {

    public boolean matches(Object bean) {
        return isEntityProvider(bean) || isRepository(bean);
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
}
