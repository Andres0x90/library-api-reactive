package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class CreateUseCase implements Function<ResourceDTO, Mono<ResourceDTO>> {
    private LibraryService service;

    @Autowired
    public CreateUseCase(LibraryService service)
    {
        this.service = service;
    }

    @Override
    public Mono<ResourceDTO> apply(ResourceDTO resourceDTO) {
        return service.create(resourceDTO);
    }
}
