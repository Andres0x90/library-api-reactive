package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.mappers.ResourceMapper;
import co.com.sofka.libraryapireactive.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Component
public class FilterBySubjectAreaUseCase implements Function<String, Flux<ResourceDTO>> {

    private ResourceRepository resourceRepository;
    private ResourceMapper resourceMapper;

    @Autowired
    public FilterBySubjectAreaUseCase(ResourceRepository resourceRepository, ResourceMapper resourceMapper)
    {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    public Flux<ResourceDTO> filterBySubjectArea(String subjectArea)
    {
        return resourceRepository.findBySubjectArea(subjectArea).map(resourceMapper::fromEntity);
    }

    @Override
    public Flux<ResourceDTO> apply(String area) {
        return filterBySubjectArea(area);
    }
}
