package co.com.sofka.libraryapireactive.usecases;

import co.com.sofka.libraryapireactive.services.LibraryService;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONValue;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

@Component
public class CheckAvailabilityUseCase implements Function<String, Mono<String>> {

    private LibraryService service;

    @Autowired
    public CheckAvailabilityUseCase(LibraryService service)
    {
        this.service = service;
    }


    @Override
    public Mono<String> apply(String id) {
        return service.checkAvailability(id)
                .map(JSONValue::toJSONString);
    }
}
