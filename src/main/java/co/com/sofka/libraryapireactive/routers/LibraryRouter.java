package co.com.sofka.libraryapireactive.routers;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.usecases.*;
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
    public RouterFunction<ServerResponse> giveBack(GiveBackUseCase
                                                       giveBackUseCase)
    {
        return route(PUT("/resources/give-back/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(
                                giveBackUseCase.apply(request.pathVariable("id")
                                ), String.class)));
    }
    @Bean
    public RouterFunction<ServerResponse> filterByType(FilterByTypeUseCase
                                                           filterByTypeUseCase)
    {
        return route(GET("/resources/filter-by-type/{type}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(
                                filterByTypeUseCase.apply(request.pathVariable("type")
                                ), ResourceDTO.class)));
    }
    @Bean
    public RouterFunction<ServerResponse> filterBySubjectArea(FilterBySubjectAreaUseCase
                                                               filterBySubjectAreaUseCase)
    {
        return route(GET("/resources/filter-by-subject-area/{subjectArea}")
                        .and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(
                                filterBySubjectAreaUseCase.apply(request.pathVariable("subjectArea")
                                ), ResourceDTO.class)));
    }
    @Bean
    public RouterFunction<ServerResponse> filterByTypeAndSubjectArea
            (FilterByTypeAndSubjectAreaUseCase filterByTypeAndSubjectAreaUseCase)
    {
        return route(GET("/resources/filter-by-type-and-subject-area")
                        .and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(
                                filterByTypeAndSubjectAreaUseCase.apply(
                                        request.queryParam("type").get(),
                                        request.queryParam("subjectArea").get()
                                ), ResourceDTO.class)));
    }
    @Bean
    public RouterFunction<ServerResponse> create(CreateUseCase
                                                       createUseCase)
    {
        return route(POST("/resources/create").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(ResourceDTO.class)
                        .flatMap(createUseCase::apply)
                        .flatMap(ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)::bodyValue));
    }
    @Bean
    public RouterFunction<ServerResponse> list(ListUseCase
                                                         listUseCase)
    {
        return route(GET("/resources/list").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(listUseCase.get(), ResourceDTO.class)));
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
    @Bean
    public RouterFunction<ServerResponse> delete(DeleteUseCase
                                                           deleteUseCase)
    {
        return route(DELETE("/resources/delete/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(
                                deleteUseCase.apply(request.pathVariable("id")
                                ), String.class)));
    }
}
