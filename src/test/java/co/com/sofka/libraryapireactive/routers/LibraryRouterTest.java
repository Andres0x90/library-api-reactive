package co.com.sofka.libraryapireactive.routers;

import co.com.sofka.libraryapireactive.collections.Resource;
import co.com.sofka.libraryapireactive.configurations.ApiRestConfig;
import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.mappers.ResourceMapper;
import co.com.sofka.libraryapireactive.repositories.ResourceRepository;
import co.com.sofka.libraryapireactive.services.LibraryService;
import co.com.sofka.libraryapireactive.usecases.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LibraryRouter.class, CheckAvailabilityUseCase.class,
        LendUseCase.class, GiveBackUseCase.class, FilterByTypeUseCase.class, FilterBySubjectAreaUseCase.class
, FilterByTypeAndSubjectAreaUseCase.class, CreateUseCase.class, FindByIdUseCase.class, ListUseCase.class,
UpdateUseCase.class, DeleteUseCase.class, LibraryService.class, ResourceRepository.class, ResourceMapper.class,
         ApiRestConfig.class})
class LibraryRouterTest {

    @MockBean
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void checkAvailability() {
    }

    @Test
    void lend() {
    }

    @Test
    void giveBack() {
    }

    @Test
    void filterByType() {
    }

    @Test
    void filterBySubjectArea() {
    }

    @Test
    void filterByTypeAndSubjectArea() {
    }

    @Test
    void create() {
        var data =  getResourceData();
        Mockito.when(resourceRepository.save(Mockito.any())).thenReturn(Mono.just(data));

        webTestClient.post().uri("/resources/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), ResourceDTO.class)
                .exchange()
                .expectBody(ResourceDTO.class)
                .value(resourceDTO ->
                {
                    assertEquals(data.getTitle(),resourceDTO.getTitle());
                    assertEquals(data.getType(),resourceDTO.getType());
                    assertEquals(data.getSubjectArea(),resourceDTO.getSubjectArea());

                });
    }

    @Test
    void list() {
        var data =  getResourceListData();
        Mockito.when(resourceRepository.findAll()).thenReturn(Flux.fromIterable(data));

        webTestClient.get().uri("/resources/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ResourceDTO.class)
                .value(resourcesDTO ->
                {
                    assertEquals(data.get(0).getTitle(),resourcesDTO.get(0).getTitle());
                    assertEquals(data.get(0).getType(),resourcesDTO.get(0).getType());
                    assertEquals(data.get(0).getSubjectArea(),resourcesDTO.get(0).getSubjectArea());

                    assertEquals(data.get(1).getTitle(),resourcesDTO.get(1).getTitle());
                    assertEquals(data.get(1).getType(),resourcesDTO.get(1).getType());
                    assertEquals(data.get(1).getSubjectArea(),resourcesDTO.get(1).getSubjectArea());

                    assertEquals(data.get(2).getTitle(),resourcesDTO.get(2).getTitle());
                    assertEquals(data.get(2).getType(),resourcesDTO.get(2).getType());
                    assertEquals(data.get(2).getSubjectArea(),resourcesDTO.get(2).getSubjectArea());
                });
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    private Resource getResourceData()
    {
        var data1 = new Resource();
        data1.setId("xxxx");
        data1.setTitle("Recurso1");
        data1.setType("book");
        data1.setSubjectArea("tech");

        return data1;
    }

    private List<Resource> getResourceListData(){
        var data1 = new Resource();
        data1.setTitle("Recurso1");
        data1.setType("book");
        data1.setSubjectArea("tech");
        var data2 = new Resource();
        data2.setTitle("Recurso1");
        data2.setType("book");
        data2.setSubjectArea("science");
        var data3 = new Resource();
        data3.setTitle("Recurso3");
        data3.setType("magazine");
        data3.setSubjectArea("tech");

        List<Resource> resources = new ArrayList<>();
        resources.add(data1);
        resources.add(data2);
        resources.add(data3);
        return resources;
    }
}