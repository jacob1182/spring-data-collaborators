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
package org.jasi.springdata.collaborators;

import org.jasi.springdata.collaborators.providers.EntityProviderBeanPostProcessor;
import org.jasi.springdata.collaborators.providers.EntityProviderMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

/**
 * Collaborator auto configuration
 *
 * @author @jacob1182
 */
@Configuration
public class CollaboratorAutoConfiguration {

    @Bean
    public EntityProviderBeanPostProcessor persistenceBeanPostProcessor(ApplicationContext context, EntityProviderMatcher entityProviderMatcher) {
        return new EntityProviderBeanPostProcessor(context, entityProviderMatcher);
    }

    @Bean
    public EntityProviderMatcher entityProviderMatcher(Supplier<Class<?>[]> collaboratorProviders) {
        return new EntityProviderMatcher(collaboratorProviders.get());
    }
}
