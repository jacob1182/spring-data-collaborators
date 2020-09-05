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
import org.jasi.springdata.collaborators.injectors.wrapper.*;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Collaborator injector handler
 *
 * @author @jacob1182
 */
public class CollaboratorInjectorHandler {

    private final CollaboratorInjector injector;
    private final List<CollaboratorWrapperInjector<?>> injectorStrategies = new ArrayList<>(asList(
            new ArrayCollaboratorWrapperInjector(),
            new IterableCollaboratorWrapperInjector(),
            new OptionalCollaboratorWrapperInjector(),
            new StreamCollaboratorWrapperInjector()
    ));

    /**
     * construct the injectors registry, the order matters.
     *
     * @param context application context
     */
    public CollaboratorInjectorHandler(ApplicationContext context) {

        this.injector =  new DefaultCollaboratorInjector(
                new EntityFactoryRegistry(context),
                new CollaboratorEntityInjector(context));

        context.getBeansOfType(CollaboratorWrapperInjector.class)
                .values()
                .forEach(injectorStrategies::add);
    }

    @SuppressWarnings("unchecked")
    public <T> T injectCollaborators(T candidate) {
        for (CollaboratorWrapperInjector<?> injectorStrategy : injectorStrategies) {
            if (injectorStrategy.canInject(candidate)) {
                CollaboratorWrapperInjector<T> typedInjectorStrategy = (CollaboratorWrapperInjector<T>) injectorStrategy;
                return typedInjectorStrategy.injectCollaboratorsInto(candidate, injector);
            }
        }

       return injector.inject(candidate);
    }
}
