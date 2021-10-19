package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.mappers.ResourceMapper;
import co.com.sofka.libraryapireactive.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class FilterByTypeAndSubjectAreaUseCase {
    private ResourceRepository resourceRepository;
    private ResourceMapper resourceMapper;

    @Autowired
    public FilterByTypeAndSubjectAreaUseCase(ResourceRepository resourceRepository, ResourceMapper resourceMapper)
    {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }


    public Flux<ResourceDTO> filterByTypeAndSubjectArea(String type, String subjectArea)
    {
        return resourceRepository.findByTypeAndSubjectArea(type, subjectArea)
                .map(resourceMapper::fromEntity);
    }

    public Flux<ResourceDTO> apply(String type, String subjectArea) {
        return filterByTypeAndSubjectArea(type, subjectArea);
    }
}
