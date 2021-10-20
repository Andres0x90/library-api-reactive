package co.com.sofka.libraryapireactive.routers;

import co.com.sofka.libraryapireactive.collections.Resource;
import co.com.sofka.libraryapireactive.configurations.ApiRestConfig;
import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.mappers.ResourceMapper;
import co.com.sofka.libraryapireactive.repositories.ResourceRepository;
import co.com.sofka.libraryapireactive.usecases.*;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LibraryRouter.class, CheckAvailabilityUseCase.class,
        LendUseCase.class, GiveBackUseCase.class, FilterByTypeUseCase.class, FilterBySubjectAreaUseCase.class
, FilterByTypeAndSubjectAreaUseCase.class, CreateUseCase.class, FindByIdUseCase.class, ListUseCase.class,
UpdateUseCase.class, DeleteUseCase.class, ResourceRepository.class, ResourceMapper.class,
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
        var data =  getResourceData();
        data.lend();
        Mockito.when(resourceRepository.findById("xxxx")).thenReturn(Mono.just(data));

        webTestClient.get().uri("/resources/check-availability/xxxx")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(stringResponse ->
                {
                    Map<String, String> response = (Map<String, String>) JSONValue.parse(stringResponse);
                    assertEquals("Este recurso no se encuentra disponible para prestar",
                            response.get("message"));
                });
    }

    @Test
    void lend() {
        var data =  getResourceData();
        Mockito.when(resourceRepository.findById("xxxx")).thenReturn(Mono.just(data));
        var dataLent = getResourceData();
        dataLent.lend();
        Mockito.when(resourceRepository.save(Mockito.any())).thenReturn(Mono.just(dataLent));

        webTestClient.put().uri("/resources/lend/xxxx")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(stringResponse ->
                {
                    Map<String, Object> response = (Map<String, Object>) JSONValue.parse(stringResponse);
                    assertEquals("Recurso prestado exitosamente",
                            response.get("message"));

                    var resourceDTO = (Map) response.get("resource");
                    assertNotNull(resourceDTO.get("dateLent"));
                });
    }

    @Test
    void giveBack() {
        var data =  getResourceData();
        data.lend();
        Mockito.when(resourceRepository.findById("xxxx")).thenReturn(Mono.just(data));
        var dataLent = getResourceData();
        dataLent.giveBack();
        Mockito.when(resourceRepository.save(Mockito.any())).thenReturn(Mono.just(dataLent));

        webTestClient.put().uri("/resources/give-back/xxxx")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(stringResponse ->
                {
                    Map<String, String> response = (Map<String, String>) JSONValue.parse(stringResponse);
                    assertEquals("Recurso devuelto exitosamente",
                            response.get("message"));
                });
    }

    @Test
    void filterByType() {
        var data =  getResourceListData();
        Mockito.when(resourceRepository.findByType("book")).thenReturn(Flux.just(data.get(0), data.get(1)));

        webTestClient.get().uri("/resources/filter-by-type/book")
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

                });
    }

    @Test
    void filterBySubjectArea() {
        var data =  getResourceListData();
        Mockito.when(resourceRepository.findBySubjectArea("tech"))
                .thenReturn(Flux.just(data.get(0), data.get(2)));

        webTestClient.get().uri("/resources/filter-by-subject-area/tech")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ResourceDTO.class)
                .value(resourcesDTO ->
                {
                    assertEquals(data.get(0).getTitle(),resourcesDTO.get(0).getTitle());
                    assertEquals(data.get(0).getType(),resourcesDTO.get(0).getType());
                    assertEquals(data.get(0).getSubjectArea(),resourcesDTO.get(0).getSubjectArea());

                    assertEquals(data.get(2).getTitle(),resourcesDTO.get(1).getTitle());
                    assertEquals(data.get(2).getType(),resourcesDTO.get(1).getType());
                    assertEquals(data.get(2).getSubjectArea(),resourcesDTO.get(1).getSubjectArea());

                });
    }

    @Test
    void filterByTypeAndSubjectArea() {
        var data =  getResourceListData();
        Mockito.when(resourceRepository.findByTypeAndSubjectArea("magazine", "tech"))
                .thenReturn(Flux.just(data.get(2)));

        webTestClient.get().uri("/resources/filter-by-type-and-subject-area?type=magazine&subjectArea=tech")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ResourceDTO.class)
                .value(resourcesDTO ->
                {
                    assertEquals(data.get(2).getTitle(),resourcesDTO.get(0).getTitle());
                    assertEquals(data.get(2).getType(),resourcesDTO.get(0).getType());
                    assertEquals(data.get(2).getSubjectArea(),resourcesDTO.get(0).getSubjectArea());
                });
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
        var data =  getResourceData();
        Mockito.when(resourceRepository.findById(data.getId())).thenReturn(Mono.just(data));

        webTestClient.get().uri("/resources/list/xxxx")
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
    void update() {
        var data =  getResourceData();
        Mockito.when(resourceRepository.findById("xxxx")).thenReturn(Mono.just(data));
        data.setTitle("recurso1 cambiado");
        Mockito.when(resourceRepository.save(Mockito.any())).thenReturn(Mono.just(data));

        webTestClient.put().uri("/resources/update").accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), ResourceDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResourceDTO.class)
                .value(resourceDTO ->
                {
                    assertEquals(data.getTitle(),resourceDTO.getTitle());
                    assertEquals(data.getType(),resourceDTO.getType());
                    assertEquals(data.getSubjectArea(),resourceDTO.getSubjectArea());
                });
    }

    @Test
    void delete() {
        var data =  getResourceData();
        Mockito.when(resourceRepository.findById("xxxx")).thenReturn(Mono.just(data));
        Mockito.when(resourceRepository.delete(Mockito.any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/resources/delete/xxxx")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(stringResponse ->
                {
                    Map<String, String> response = (Map<String, String>) JSONValue.parse(stringResponse);
                    assertEquals("Recurso eliminado correctamente", response.get("message"));
                });
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