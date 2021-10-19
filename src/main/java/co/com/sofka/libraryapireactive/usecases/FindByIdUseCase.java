package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.mappers.ResourceMapper;
import co.com.sofka.libraryapireactive.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class FindByIdUseCase implements Function<String, Mono<ResourceDTO>> {

    private ResourceRepository resourceRepository;
    private ResourceMapper resourceMapper;

    @Autowired
    public FindByIdUseCase(ResourceRepository resourceRepository, ResourceMapper resourceMapper)
    {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    public Mono<ResourceDTO> findById(String id)
    {
        return resourceRepository.findById(id)
                .hasElement().flatMap(state ->
                {
                    if (!state)
                        throw new RuntimeException("Este recurso no ha sido encontrado");
                    return resourceRepository.findById(id);
                })
                .map(resourceMapper::fromEntity);
    }

    @Override
    public Mono<ResourceDTO> apply(String id) {
        return findById(id);
    }
}
