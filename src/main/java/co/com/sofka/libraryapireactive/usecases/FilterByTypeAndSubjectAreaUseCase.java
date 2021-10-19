package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class FilterByTypeAndSubjectAreaUseCase {
    private LibraryService service;

    @Autowired
    public FilterByTypeAndSubjectAreaUseCase(LibraryService service)
    {
        this.service = service;
    }

    public Flux<ResourceDTO> apply(String type, String subjectArea) {
        return service.filterByTypeAndSubjectArea(type, subjectArea);
    }
}
