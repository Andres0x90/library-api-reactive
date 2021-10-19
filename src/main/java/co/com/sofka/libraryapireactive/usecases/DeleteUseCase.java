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
public class DeleteUseCase implements Function<String, Mono<String>> {
    private ResourceRepository resourceRepository;
    private ResourceMapper resourceMapper;

    @Autowired
    public DeleteUseCase(ResourceRepository resourceRepository, ResourceMapper resourceMapper)
    {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    public Mono<Map<String, String>> delete(String id){
        return new FindByIdUseCase(resourceRepository, resourceMapper).apply(id).map(resourceMapper::fromDTO)
                .map(resource ->
                {
                    var response = new HashMap<String, String>();
                    resourceRepository.delete(resource).subscribe();
                    response.put("message", "Recurso eliminado correctamente");
                    return response;
                });
    }

    @Override
    public Mono<String>apply(String id) {
        return delete(id).map(JSONValue::toJSONString);
    }
}
