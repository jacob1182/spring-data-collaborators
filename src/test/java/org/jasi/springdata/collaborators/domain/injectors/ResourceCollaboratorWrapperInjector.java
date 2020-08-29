package org.jasi.springdata.collaborators.domain.injectors;

import org.jasi.springdata.collaborators.injectors.CollaboratorInjector;
import org.jasi.springdata.collaborators.injectors.wrapper.CollaboratorWrapperInjector;
import org.springframework.stereotype.Component;

@Component
public class ResourceCollaboratorWrapperInjector implements CollaboratorWrapperInjector<Resource<?>> {

    @Override
    public boolean canInject(Object candidate) {
        return candidate instanceof Resource;
    }

    @Override
    public Resource<?> injectCollaboratorsInto(Resource<?> candidate, CollaboratorInjector injector) {
        injector.inject(candidate.getData());
        return candidate;
    }
}
