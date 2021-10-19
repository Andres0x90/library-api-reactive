package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Component
public class ListUseCase implements Supplier<Flux<ResourceDTO>> {
    private LibraryService service;

    @Autowired
    public ListUseCase(LibraryService service)
    {
        this.service = service;
    }

    @Override
    public Flux<ResourceDTO> get() {
        return service.list();
    }
}
