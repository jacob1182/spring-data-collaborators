package org.jasi.springdata.collaborators.providers;

import org.jasi.springdata.collaborators.annotation.CollaboratorConfig;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.function.Supplier;

@Configuration
public class EntityProviderRegistrar implements ImportBeanDefinitionRegistrar {

    private static Class<?>[] providers = new Class<?>[0];

    @Bean
    public Supplier<Class<?>[]> collaboratorProviders() {
        return () -> providers;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        String annotation = CollaboratorConfig.class.getName();

        if (annotationMetadata.hasAnnotation(annotation)) {
            providers = AnnotationAttributes
                    .fromMap(annotationMetadata.getAnnotationAttributes(annotation, false))
                    .getClassArray("providers");
        }
    }
}
