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

import org.jasi.springdata.collaborators.injectors.wrapper.ResponseEntityCollaboratorWrapperInjector;
import org.jasi.springdata.collaborators.providers.EntityProviderBeanPostProcessor;
import org.jasi.springdata.collaborators.providers.EntityProviderMatcherRegistry;
import org.jasi.springdata.collaborators.providers.matchers.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.Repository;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.function.Supplier;

/**
 * Collaborator auto configuration
 *
 * @author @jacob1182
 */
@Configuration
public class CollaboratorAutoConfiguration {

    @Bean
    public EntityProviderBeanPostProcessor persistenceBeanPostProcessor(ApplicationContext context, EntityProviderMatcherRegistry entityProviderMatcherRegistry) {
        return new EntityProviderBeanPostProcessor(context, entityProviderMatcherRegistry);
    }

    @Bean
    public EntityProviderMatcherRegistry entityProviderMatcher(List<EntityProviderMatcher> entityProviderMatchers) {
        return new EntityProviderMatcherRegistry(entityProviderMatchers);
    }

    @Bean
    public CustomEntityProviderMatcher customEntityProviderMatcher() {
        return new CustomEntityProviderMatcher();
    }

    @Bean
    public ThirdPartyEntityProviderMatcher thirdPartyEntityProviderMatcher(Supplier<Class<?>[]> collaboratorProviders) {
        return new ThirdPartyEntityProviderMatcher(collaboratorProviders.get());
    }

    @Bean
    @ConditionalOnClass(Repository.class)
    public RepositoryEntityProviderMatcher repositoryEntityProviderMatcher() {
        return new RepositoryEntityProviderMatcher();
    }

    @Bean
    @ConditionalOnClass(RestOperations.class)
    public RestOperationsEntityProviderMatcher restOperationsEntityProviderMatcher() {
        return new RestOperationsEntityProviderMatcher();
    }

    @Bean
    @ConditionalOnClass(RestOperations.class)
    public ResponseEntityCollaboratorWrapperInjector responseEntityCollaboratorWrapperInjector() {
        return new ResponseEntityCollaboratorWrapperInjector();
    }
}
