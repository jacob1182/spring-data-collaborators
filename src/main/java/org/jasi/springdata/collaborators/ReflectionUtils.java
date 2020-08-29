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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Reflection Utility class
 *
 * @author @jacob1182
 */
class ReflectionUtils {

    static Class<?> firstGenericArgumentType(Class<?> genericInterface, Class<?> target) {

        for (Class<?> targetClass = target; targetClass != Object.class;  targetClass = target.getSuperclass()) {

            for (Type genericType : targetClass.getGenericInterfaces()) {

                ParameterizedType theType = from(genericInterface, genericType);

                if (theType == null)
                    continue;

                Type[] typeArguments = theType.getActualTypeArguments();

                if (typeArguments.length > 0)
                    return (Class<?>) typeArguments[0];
            }
        }

        return null;
    }

    private static ParameterizedType from(Class<?> genericInterface, Type type) {

        if(type instanceof ParameterizedType)
            return getExpectedTypeOrNull(genericInterface, (ParameterizedType) type);

        Type[] genericTypes = ((Class<?>) type).getGenericInterfaces();

        if(genericTypes.length == 0)
            return null;

        return from(genericInterface, genericTypes[0]);
    }

    private static ParameterizedType getExpectedTypeOrNull(Class<?> genericInterface, ParameterizedType type) {
        Class<?> rawType = (Class<?>) type.getRawType();
        return genericInterface.isAssignableFrom(rawType) ? type : null;
    }
}
