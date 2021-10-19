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
public class GiveBackUseCase implements Function<String, Mono<String>> {
    private ResourceRepository resourceRepository;
    private ResourceMapper resourceMapper;

    @Autowired
    public GiveBackUseCase(ResourceRepository resourceRepository, ResourceMapper resourceMapper)
    {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }
    public Mono<Map<String, String>> giveBack(String id)
    {
        return new FindByIdUseCase(resourceRepository, resourceMapper).apply(id).map(resourceMapper::fromDTO)
                .flatMap(resource ->
                {
                    if (resource.isLent()) {
                        resource.giveBack();
                        return resourceGivenBackSuccessResponse(resource);
                    }

                    return resourceGivenBackFailureResponse(resource);
                });
    }

    private Mono<Map<String, String>> resourceGivenBackSuccessResponse(Resource resource)
    {
        return Mono.just(resource).map(resourceMapper::fromEntity)
                .flatMap(new UpdateUseCase(resourceRepository,resourceMapper)::apply)
                .map(resourceDTO ->
                {
                    var response = new HashMap<String, String>();
                    response.put("message", "Recurso devuelto exitosamente");
                    return response;
                });
    }
    private Mono<Map<String, String>> resourceGivenBackFailureResponse(Resource resource)
    {
        return Mono.just(new HashMap<String, String>())
                .map(response ->
                {
                    response.put("message", "Este recurso no se encuentra prestado");
                    return response;
                });
    }


    @Override
    public Mono<String>apply(String id) {
        return giveBack(id).map(JSONValue::toJSONString);
    }
}
