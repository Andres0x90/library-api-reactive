package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.mappers.ResourceMapper;
import co.com.sofka.libraryapireactive.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Component
public class ListUseCase implements Supplier<Flux<ResourceDTO>> {
    private ResourceRepository resourceRepository;
    private ResourceMapper resourceMapper;

    @Autowired
    public ListUseCase(ResourceRepository resourceRepository, ResourceMapper resourceMapper)
    {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    public Flux<ResourceDTO> list()
    {
        return resourceRepository.findAll().map(resourceMapper::fromEntity);
    }

    @Override
    public Flux<ResourceDTO> get() {
        return list();
    }
}
