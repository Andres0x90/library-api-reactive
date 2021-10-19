package co.com.sofka.libraryapireactive.usecases;

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
public class CheckAvailabilityUseCase implements Function<String, Mono<String>> {

    private ResourceRepository resourceRepository;
    private ResourceMapper resourceMapper;

    @Autowired
    public CheckAvailabilityUseCase(ResourceRepository resourceRepository, ResourceMapper resourceMapper)
    {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }
    public Mono<Map<String, Object>> checkAvailability(String id)
    {
        return new FindByIdUseCase(resourceRepository, resourceMapper).apply(id)
                .map(resourceMapper::fromDTO)
                .flatMap(resource ->
                {
                    if (resource.isLent())
                        return new LendUseCase(resourceRepository, resourceMapper)
                                .getResourceLentFailureResponse(resource);

                    return getAvailableMessageResponse();
                });
    }

    private Mono<HashMap<String, Object>> getAvailableMessageResponse() {
        return Mono.just(new HashMap<String, Object>())
                .map(response ->
                {
                    response.put("message", "Este recurso se encuentra disponible");
                    return response;
                });
    }

    @Override
    public Mono<String> apply(String id) {
        return checkAvailability(id)
                .map(JSONValue::toJSONString);
    }
}
