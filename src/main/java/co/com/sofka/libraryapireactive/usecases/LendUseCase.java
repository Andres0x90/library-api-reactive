package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.collections.Resource;
import co.com.sofka.libraryapireactive.mappers.ResourceMapper;
import co.com.sofka.libraryapireactive.repositories.ResourceRepository;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class LendUseCase implements Function<String, Mono<String>> {
    private ResourceRepository resourceRepository;
    private ResourceMapper resourceMapper;

    @Autowired
    public LendUseCase(ResourceRepository resourceRepository, ResourceMapper resourceMapper)
    {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    public Mono<Map<String, Object>> lend(String id)
    {
        return new FindByIdUseCase(resourceRepository, resourceMapper).apply(id).map(resourceMapper::fromDTO)
                .flatMap(resource ->
                {
                    if (resource.isLent())
                        return getResourceLentFailureResponse(resource);

                    resource.lend();
                    return getSucessfulLentResponse(resource);
                });
    }

    public Mono<HashMap<String, Object>> getSucessfulLentResponse(Resource resource) {
        return Mono.just(resource)
                .map(resourceMapper::fromEntity)
                .flatMap(resourceDTO ->
                        new UpdateUseCase(resourceRepository, resourceMapper).apply(resourceDTO)
                                .map(resourceUpdated ->
                                {
                                    var response = new HashMap<String, Object>();
                                    response.put("message", "Recurso prestado exitosamente");
                                    response.put("resource", resourceUpdated);
                                    return  response;
                                })
                );
    }

    public Mono<Map<String, Object>> getResourceLentFailureResponse(Resource resource)
    {
        return Mono.just(new HashMap<String, Object>())
                .map(response ->
                {
                    response.put("message", "Este recurso no se encuentra disponible para prestar");
                    response.put("resource", resourceMapper.fromEntity(resource));
                    return response;
                });
    }

    @Override
    public Mono<String> apply(String id) {
        return lend(id).map(JSONValue::toJSONString);
    }
}
