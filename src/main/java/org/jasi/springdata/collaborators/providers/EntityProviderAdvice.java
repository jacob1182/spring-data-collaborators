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
package org.jasi.springdata.collaborators.providers;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jasi.springdata.collaborators.injectors.CollaboratorInjectorHandler;
import org.springframework.aop.framework.Advised;

import java.lang.reflect.Method;

/**
 * Entity Provider proxy
 *
 * @author @jacob1182
 */
class EntityProviderAdvice implements MethodInterceptor {

    private final CollaboratorInjectorHandler injectorHandler;

    EntityProviderAdvice(CollaboratorInjectorHandler injectorHandler) {
        this.injectorHandler = injectorHandler;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();

        if(isResultAnInvalidEntity(invocation.getMethod(), result))
            return result;

        return injectorHandler.injectCollaborators(result);
    }

    private boolean isResultAnInvalidEntity(Method method, Object result) {

        return result == null
                || result.getClass().getName().startsWith("java.lang") // exclude primitives
                || method.getDeclaringClass().equals(Advised.class);
    }
}
