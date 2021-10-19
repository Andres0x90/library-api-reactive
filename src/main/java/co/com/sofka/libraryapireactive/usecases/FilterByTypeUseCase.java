package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Component
public class FilterByTypeUseCase implements Function<String, Flux<ResourceDTO>> {

    private LibraryService service;

    @Autowired
    public FilterByTypeUseCase(LibraryService service)
    {
        this.service = service;
    }

    @Override
    public Flux<ResourceDTO> apply(String type) {
        return service.filterByType(type);
    }
}
