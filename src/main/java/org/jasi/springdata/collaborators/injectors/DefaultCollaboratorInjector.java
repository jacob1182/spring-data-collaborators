/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jasi.springdata.collaborators.injectors;

import org.jasi.springdata.collaborators.CollaboratorEntityInjector;
import org.jasi.springdata.collaborators.EntityFactoryRegistry;

/**
 * Abstract collaborator injector
 *
 * @author @jacob1182
 */
public class DefaultCollaboratorInjector implements CollaboratorInjector {

    private final EntityFactoryRegistry entityFactoryRegistry;

    private final CollaboratorEntityInjector entityInjector;


    public DefaultCollaboratorInjector(EntityFactoryRegistry entityFactoryRegistry, CollaboratorEntityInjector entityInjector) {
        this.entityFactoryRegistry = entityFactoryRegistry;
        this.entityInjector = entityInjector;
    }

    @Override
    public  <T> T inject(T candidate) {

        candidate = injectCollaborators(candidate);

        candidate = reconstituteAggregate(candidate);

        return candidate;
    }

    private <T> T injectCollaborators(T candidate) {
        return entityInjector.injectCollaborators(candidate);
    }

    private <T> T reconstituteAggregate(T candidate) {

        @SuppressWarnings("unchecked")
        Class<T> aggregateClass = (Class<T>) candidate.getClass();

        return entityFactoryRegistry.factoryBy(aggregateClass)
                .map(factory -> factory.reconstitute(candidate))
                .orElse(candidate);
    }
}
