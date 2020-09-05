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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jasi.springdata.collaborators.domain.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Load beans into the context
 *
 * @author @jacob1182
 */
@Configuration
@ComponentScan(basePackages = "org.jasi.springdata.collaborators")
public class CollaboratorTestConfig {

    @Bean
    public RestTemplate testRestTemplate() {
        return ProxyUtils.createProxy(new RestTemplate(), true, new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                Class<?> resultClass = methodInvocation.getMethod().getReturnType();

                if (resultClass.isAssignableFrom(Object.class))
                    return Order.of("Onion");

                if(resultClass.isAssignableFrom(ResponseEntity.class))
                    return ResponseEntity.ok(Order.of("Onion"));

                return null;
            }
        });
    }
}
