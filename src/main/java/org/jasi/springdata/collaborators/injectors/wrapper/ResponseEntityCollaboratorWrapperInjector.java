package org.jasi.springdata.collaborators.injectors.wrapper;

import org.jasi.springdata.collaborators.injectors.CollaboratorInjector;
import org.springframework.http.ResponseEntity;

public class ResponseEntityCollaboratorWrapperInjector implements CollaboratorWrapperInjector<ResponseEntity<?>> {

    @Override
    public boolean canInject(Object candidate) {
        return candidate instanceof ResponseEntity;
    }

    @Override
    public ResponseEntity<?> injectCollaboratorsInto(ResponseEntity<?> candidate, CollaboratorInjector injector) {
        injector.inject(candidate.getBody());
        return candidate;
    }
}
