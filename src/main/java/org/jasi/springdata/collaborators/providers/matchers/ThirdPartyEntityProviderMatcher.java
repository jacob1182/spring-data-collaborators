package org.jasi.springdata.collaborators.providers.matchers;

import java.util.stream.Stream;

public class ThirdPartyEntityProviderMatcher implements EntityProviderMatcher {

    private final Class<?>[] providers;

    public ThirdPartyEntityProviderMatcher(Class<?>[] providers) {
        this.providers = providers;
    }

    @Override
    public boolean matches(Object bean) {
        return Stream.of(providers).anyMatch(type -> type.isInstance(bean));
    }
}
