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
package org.jasi.springdata.collaborators.injectors.wrapper;

import org.jasi.springdata.collaborators.injectors.CollaboratorInjector;

import java.util.stream.Stream;

/**
 * Stream collaborator injector
 *
 * @author @jacob1182
 */
public class StreamCollaboratorWrapperInjector implements CollaboratorWrapperInjector<Stream<?>> {

    @Override
    public boolean canInject(Object candidate) {
        return candidate instanceof Stream;
    }

    @Override
    public Stream<?> injectCollaboratorsInto(Stream<?> candidate, CollaboratorInjector injector) {
        return candidate.map(injector::inject);
    }
}
