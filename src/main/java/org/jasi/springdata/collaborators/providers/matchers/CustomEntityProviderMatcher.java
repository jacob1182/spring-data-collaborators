package org.jasi.springdata.collaborators.providers.matchers;

import org.jasi.springdata.collaborators.providers.EntityProvider;

public class CustomEntityProviderMatcher implements EntityProviderMatcher{
    @Override
    public boolean matches(Object bean) {
        return bean instanceof EntityProvider
                || bean.getClass()
                .isAnnotationPresent(org.jasi.springdata.collaborators.annotation.EntityProvider.class);
    }
}
