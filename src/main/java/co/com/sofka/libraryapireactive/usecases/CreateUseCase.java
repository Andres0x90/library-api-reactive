package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.mappers.ResourceMapper;
import co.com.sofka.libraryapireactive.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class CreateUseCase implements Function<ResourceDTO, Mono<ResourceDTO>> {
    private ResourceRepository resourceRepository;
    private ResourceMapper resourceMapper;

    @Autowired
    public CreateUseCase(ResourceRepository resourceRepository, ResourceMapper resourceMapper)
    {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    public Mono<ResourceDTO> create(ResourceDTO resourceDTO)
    {
        return Mono.just(resourceDTO).map(resourceMapper::fromDTO)
                .flatMap(resourceRepository::save).map(resourceMapper::fromEntity);
    }

    @Override
    public Mono<ResourceDTO> apply(ResourceDTO resourceDTO) {
        return create(resourceDTO);
    }
}
