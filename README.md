# Content
- [Usage](#usage)
    - [@Collaborator](#collaborator)
    - [@Qualifiers](#qualifiers)
    - [EntityFactory](#entityfactory)
- [Entity Providers](#entity-provider)
    - [Repositories](#repositories)
    - [RestTemplate](#resttemplate)
    - [Custom Providers](#custom-providers)
        - [EntityProvider](#entityprovider)
        - [Third-Party Providers](#third-party-providers)
- [Wrapper injectors](#wrapper-injectors)
    - [Supported wrapper injectors](#supported-wrapper-injectors)
    - [Custom wrapper injectors](#custom-wrapper-injectors)
- [Proxies](#proxies)
    - [Supported Proxy Handlers](#supported-proxy-handlers)
    - [Custom Proxy Handlers](#custom-proxy-handler)
- [Performance Considerations](#performance-considerations)  
       
## Usage
### @Collaborator

### @Qualifiers

### EntityFactory

Useful whenever is needed a more complex entity reconstitution.
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


### Repositories

- Repository interface
- Any class annotated with @Repository
- 

Note it doesn't work for  
### RestTemplate


### Custom Providers


#### EntityProvider
#### Third-Party Providers
```java
@Configuration
@CollaboratorConfig(
    providers = {OrderFileProvider.class, MongoTemplate.class},
    injectors = {CustomInjector.class}
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

