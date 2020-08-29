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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasi.springdata.collaborators.annotation.Collaborator;
import org.jasi.springdata.collaborators.extractor.ProxyTargetExtractor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.jasi.springdata.collaborators.ProxyUtils.getProxyTarget;
import static org.jasi.springdata.collaborators.ReflectionUtils.setFieldValue;

/**
 * Collaborator entity registry
 *
 * @author @jacob1182
 */
public class CollaboratorEntityInjector {

    private static final Log logger = LogFactory.getLog(CollaboratorEntityInjector.class);

    private final MultiValueMap<Class<?>, Field> collaboratorFields = new LinkedMultiValueMap<>();

    private final ApplicationContext context;

    private final Collection<ProxyTargetExtractor> proxyTargetExtractors;

    public CollaboratorEntityInjector(ApplicationContext context) {
        this.context = context;
        this.proxyTargetExtractors = context.getBeansOfType(ProxyTargetExtractor.class).values();
    }

    public <T> T injectCollaborators(T entity) {

        T theEntity = extractProxyTarget(entity);

        List<Field> fields = getCollaboratorFields(theEntity);

        for (Field field : fields) {
            Object collaborator = getCollaboratorInstance(field);
            setFieldValue(theEntity, field, collaborator);
        }

        return entity;
    }

    private <T, R extends T> R extractProxyTarget(T entity) {
        for (ProxyTargetExtractor proxyTargetExtractor: proxyTargetExtractors) {
            if (proxyTargetExtractor.support(entity))
                return proxyTargetExtractor.extractTargetFrom(entity);
        }

        return getProxyTarget(entity);
    }

    private <T> List<Field> getCollaboratorFields(T entity) {
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) entity.getClass();

        if(!collaboratorFields.containsKey(entityClass)) {

            List<Field> collaboratorFields = getCollaboratorFields(entityClass);

            this.collaboratorFields.put(entityClass, collaboratorFields);
        }

        return collaboratorFields.get(entityClass);
    }

    private Object getCollaboratorInstance(Field field) {

        if(field.isAnnotationPresent(Qualifier.class)) {
            String qualifierName = field.getAnnotation(Qualifier.class).value();
            return context.getBean(qualifierName, field.getType());
        }

        return context.getBean(field.getType());
    }

    private List<Field> getCollaboratorFields(Class<?> entityClass) {

        Predicate<Field> isCollaborator = field -> field.isAnnotationPresent(Collaborator.class);

        return Stream.of(entityClass.getDeclaredFields())
                .filter(isCollaborator)
                .collect(toList());
    }
}