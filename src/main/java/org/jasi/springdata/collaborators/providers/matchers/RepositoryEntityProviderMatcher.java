package org.jasi.springdata.collaborators.providers.matchers;

import org.springframework.data.repository.Repository;

public class RepositoryEntityProviderMatcher implements EntityProviderMatcher{
    @Override
    public boolean matches(Object bean) {
        return bean instanceof Repository
                || bean.getClass()
                .isAnnotationPresent(org.springframework.stereotype.Repository.class);
    }
}
