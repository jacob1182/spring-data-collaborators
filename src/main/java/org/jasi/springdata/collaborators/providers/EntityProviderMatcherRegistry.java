package org.jasi.springdata.collaborators.providers;

import org.jasi.springdata.collaborators.providers.matchers.EntityProviderMatcher;

import java.util.List;

public class EntityProviderMatcherRegistry {

    private final List<EntityProviderMatcher> entityProviderMatchers;

    public EntityProviderMatcherRegistry(List<EntityProviderMatcher> entityProviderMatchers) {
        this.entityProviderMatchers = entityProviderMatchers;
    }

    public boolean matches(Object bean) {
        return entityProviderMatchers.stream().anyMatch(matcher -> matcher.matches(bean));
    }
}
