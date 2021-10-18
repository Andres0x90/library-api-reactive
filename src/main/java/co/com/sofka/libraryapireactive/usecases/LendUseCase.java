package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.services.LibraryService;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class LendUseCase implements Function<String, Mono<String>> {
    private LibraryService service;

    @Autowired
    public LendUseCase(LibraryService service)
    {
        this.service = service;
    }


    @Override
    public Mono<String> apply(String id) {
        return service.lend(id)
                .map(JSONValue::toJSONString);
    }
}
