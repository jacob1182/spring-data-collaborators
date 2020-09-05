package org.jasi.springdata.collaborators.providers.matchers;

import org.springframework.web.client.RestOperations;

public class RestOperationsEntityProviderMatcher implements EntityProviderMatcher {
    @Override
    public boolean matches(Object bean) {
        return bean instanceof RestOperations;
    }
}
