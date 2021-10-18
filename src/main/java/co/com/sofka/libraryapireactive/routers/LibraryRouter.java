package co.com.sofka.libraryapireactive.routers;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.usecases.CheckAvailabilityUseCase;
import co.com.sofka.libraryapireactive.usecases.FindByIdUseCase;
import co.com.sofka.libraryapireactive.usecases.LendUseCase;
import co.com.sofka.libraryapireactive.usecases.UpdateUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LibraryRouter {
    @Bean
    public RouterFunction<ServerResponse> checkAvailability(CheckAvailabilityUseCase
                                                                        checkAvailabilityUseCase)
    {
        return route(GET("/resources/check-availability/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(
                                checkAvailabilityUseCase.apply(request.pathVariable("id")
                                        ), String.class)));
    }
    @Bean
    public RouterFunction<ServerResponse> lend(LendUseCase
                                               lendUseCase)
    {
        return route(PUT("/resources/lend/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(
                                lendUseCase.apply(request.pathVariable("id")
                                ), String.class)));
    }
    @Bean
    public RouterFunction<ServerResponse> findById(FindByIdUseCase
                                                       findByIdUseCase)
    {
        return route(GET("/resources/list/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(
                                findByIdUseCase.apply(request.pathVariable("id")
                                ), ResourceDTO.class)));
    }
    @Bean
    public RouterFunction<ServerResponse> update(UpdateUseCase
                                                           updateUseCase)
    {
        return route(PUT("/resources/update").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(ResourceDTO.class)
                        .flatMap(updateUseCase::apply)
                        .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue));
    }
}
