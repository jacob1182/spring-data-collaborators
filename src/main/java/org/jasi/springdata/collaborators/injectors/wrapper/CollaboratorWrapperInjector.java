package org.jasi.springdata.collaborators.injectors.wrapper;

import org.jasi.springdata.collaborators.injectors.CollaboratorInjector;

public interface CollaboratorWrapperInjector<T> {
    boolean canInject(Object candidate);
    T injectCollaboratorsInto(T candidate, CollaboratorInjector injector);
}
