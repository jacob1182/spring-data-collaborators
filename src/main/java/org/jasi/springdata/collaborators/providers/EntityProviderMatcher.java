package org.jasi.springdata.collaborators.providers;

import org.springframework.data.repository.Repository;

public class EntityProviderMatcher {

    public boolean matches(Object bean) {
        return bean instanceof EntityProvider
            || isRepository(bean);
    }

    private boolean isRepository(Object bean) {
        return bean instanceof Repository
            || bean.getClass()
                .isAnnotationPresent(org.springframework.stereotype.Repository.class);
    }
}
