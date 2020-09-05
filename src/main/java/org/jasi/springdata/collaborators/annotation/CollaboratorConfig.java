package org.jasi.springdata.collaborators.annotation;

import org.jasi.springdata.collaborators.providers.EntityProviderRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(EntityProviderRegistrar.class)
public @interface CollaboratorConfig {
    Class<?>[] providers() default {};
}
