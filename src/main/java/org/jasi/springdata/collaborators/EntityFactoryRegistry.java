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

import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Persistence aggregate factory registry
 *
 * @author @jacob1182
 */
public class EntityFactoryRegistry {

    private final Map<Class<?>, EntityFactory<?>> factories = new HashMap<>();

    private final ApplicationContext context;

    private boolean initialized;

    public EntityFactoryRegistry(ApplicationContext context) {
        this.context = context;
    }

    public <T> Optional<EntityFactory<T>> factoryBy(Class<T> aggregateClass) {

        loadFactories();

        // Warnings are suppressed safely
        // as it was checked at the factories map construction time
        @SuppressWarnings("unchecked")
        EntityFactory<T> factory = (EntityFactory<T>) factories.get(aggregateClass);

        if(factory == null)
            return Optional.empty();

        return Optional.of(factory);
    }

    private void loadFactories() {

        if(initialized) return;

        initialized = true;

        Class<EntityFactory> beanType = EntityFactory.class;
        Map<String, EntityFactory> beans = context.getBeansOfType(beanType);

        for (String beanName: beans.keySet()) {

            EntityFactory<?> bean = beans.get(beanName);
            Class<?> aggregateClass = ReflectionUtils.firstGenericArgumentType(beanType, bean.getClass());

            factories.put(aggregateClass, bean);
        }
    }
}
