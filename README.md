# Spring Data Collaborator

Spring Data Collaborator provides a solution to inject collaborators to your domain model when retrieving them from an external provider, such us Repository, RestTemplate or a custom provider.

This module is completely customizable.

#### Acknowledgment
After applying DDD approach you wil find that Domain models sometimes needs collaborator instances to accomplish its purpose. The best way to deal with it is to separate your Domain model from the Data model and inject the dependencies during the creation of your Domain model, but it may introduce duplication of code on both models, and you have to maintain them every time one of them change.

A messy solution is to use the same model for both scenarios. This will solve the DRY violation but introduces Single responsibility principle violation because the now unique model have to behave as a Domain model and as a Data model at the same time. In the case that Data model has a different behavior than the Domain model it will become too complex to maintain.

For simple scenarios when your model has no complex Data model behavior, and the Data model comply with the Domain model, you can use the second approach. But, unfortunately ORMs and other providers are not able to inject beans to the model. That is the purpose of this module.

## Features
- [Usage](#usage)
    - [@Collaborator](#collaborator)
    - [@Qualifiers](#qualifiers)
    - [EntityFactory](#entityfactory)
- [Entity Providers](#entity-provider)
    - [Default Entity Providers](#default-entity-providers)
    - [Custom Entity Providers](#custom-providers)
- [Wrapper injectors](#wrapper-injectors)
    - [Supported wrapper injectors](#supported-wrapper-injectors)
    - [Custom wrapper injectors](#custom-wrapper-injectors)
- [Proxies](#proxies)
    - [Supported Proxy Handlers](#supported-proxy-handlers)
    - [Custom Proxy Handlers](#custom-proxy-handler)
- [Performance Considerations](#performance-considerations)  
       
## Usage

### @Collaborator

Use the `@Collaborator` annotation to specify the property to be injected
```java
@Document
public class Order {

    @Id
    private String id;

    private String name;

    @Collaborator
    private NotificationService notificationService;
}
```

### @Qualifiers

Use `@Qualifiers` annotation to specify the bean name to inject

```java
@Document
public class Order {

    @Id
    private String id;

    private String name;

    @Collaborator
    private NotificationService notificationService;

    @Collaborator
    @Qualifier("jsonStoreInfoService")
    private StoreInfoService storeInfo;
}
```

### EntityFactory

Use EntityFactory interface for complexer entity reconstitution.
```java
@Component
public class OrderItemFactory implements EntityFactory<OrderItem> {
    private DependentService dependentService;

    // constructor

    @Override
    public OrderItem reconstitute(OrderItem element) {

        // some setting up code
        element.with(dependentService);
        // some setting up code

        return element;
    }
}
```
When performing operations over a model provider, it will use the `OrderItemFactory` to reconstitutes the `OrderItem` element.
 
```java
class OrderItemFactoryTest {

    OrderItemRepository repository; // the model provider
    
    @Test
    void shouldReconstituteModel() {
        String productId = ""; // productId
        repository.findById(productId)
                  .ifPresent(orderItem -> 
                        assertNotNull(orderItem.getDependentService()));
    }
}
```

## Entity Providers

Spring Data Collaborators intercept provider's returned values and inject the collaborator instances into them. Some providers are supported by default.

_Note: Providers should be a spring bean instance._

### Default Entity Providers
- Beans that implements `Repository` interface or is annotated with `@Repository`
- `RestTemplate`s

### Custom Entity Providers

For personalized entity providers is possible to do it in 3 ways
- Implementing the `EntityProvider` interface
- Adding the `@EntityProvider` annotation
- Register them using the `@CollaboratorConfig` annotation for configuration class

The last case is useful when the entity provider class is in a third party library.
  
__Using the `EntityProvider` inteface__
```java
@Component
public class OrderFileReader implements EntityProvider {

    public Order[] readAll() {
        //...
    }
}
```

__Using the `@EntityProvider` annotation__
```java
@Component
@EntityProvider
public class OrderFileReader {

    public Order[] readAll() {
        //...
    }
}
```
__Using the `@CollaboratorConfig` annotation for third party entity providers__
```java
@Configuration
@CollaboratorConfig(
    providers = {OrderFileProvider.class, MongoTemplate.class}
)
public class Configuration {}
```

## Wrapper Injectors

Injectors are to inject collaborators into the entity.

### Supported Wrapper Injectors

Objects sometimes comes inside of a wrapper object, in order to inject the collaborators it needs to use an injector for the specific wrapper. Otherwise it will be treated as a bare object.

By default, there are some supported wrapper injectors for the following wrappers:

- Array
- Iterables
- Stream (Java 8+)
- Optional (Java 8+)


### Custom Wrapper Injectors

For non-supported wrappers

```java
@Component
public class ResourceWrapperInjector implements CollaboratorWrapperInjector<Resource<?>> {

    @Override
    public boolean canInject(Object candidate) {
        return candidate instanceof Resource;
    }

    @Override
    public Resource<?> injectCollaboratorsInto(Resource<?> candidate, CollaboratorInjector injector) {
        injector.inject(candidate.getData());
        return candidate;
    }
}
```

## Proxies
Spring data collaborators allows you to inject collaborators also in proxy objects, such us data projections. Proxy objects are created in many ways using strategies such as InvocationHandlers or Spring Advices. That is why is not possible to support all kind of generated proxy for that reason. 
### Supported Proxy Handlers
Some Proxy strategy are supported by default.
- CglibAopProxy
- JdkDynamicAopProxy

### Custom Proxy Handler

In order to support a customized proxy is necessary to implement the interface `ProxyTargetExtractor` to extract the target object in order to inject the collaborators. Then configure to use the custom proxy target extractor.

```java
@Component
public class CustomProxyTargetExtractor implements ProxyTargetExtractor {
    
    public boolean support(Object proxy) {
        boolean isSupported = false;
        // compute whether the proxy is sopported by the extractor
        return isSupported;
    }

    public Object extractTargetFrom(Object proxy) {
        Object target = null;
        // extract proxy target object from proxy
        return target;
    }
}
```
### Performance Considerations

Injecting collaborators require an extra computation work, which it means use it with caution. Bellow some considerations that impact in the performance.

- Retrieving big collections from the entity provider.
- `@Collaborator` fields are cached the first time an entity type is retrieved from the entity provider. Too many retrieved entity types can impact in the memory usage.
- Too many `ProxyTargetExtractor`. Use Spring proxy (`ProxyFactory`) strategy as much as possible.

