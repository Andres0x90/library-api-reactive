package co.com.sofka.libraryapireactive.services;

import co.com.sofka.libraryapireactive.dtos.ResourceDTO;
import co.com.sofka.libraryapireactive.collections.Resource;
import co.com.sofka.libraryapireactive.mappers.ResourceMapper;
import co.com.sofka.libraryapireactive.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LibraryService {
    private ResourceRepository resourceRepository;
    private ResourceMapper resourceMapper;

    @Autowired
    public LibraryService(ResourceRepository resourceRepository, ResourceMapper resourceMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper =resourceMapper;
    }

    public Mono<Map<String, Object>> checkAvailability(String id)
    {
       return findById(id).map(resourceMapper::fromDTO)
                .flatMap(resource ->
                {
                    if (resource.isLent())
                        return resourceLentResponse(resource);

                    return getAvailableMessageResponse();
                });
    }

    private Mono<HashMap<String, Object>> getAvailableMessageResponse() {
        return Mono.just(new HashMap<String, Object>())
                        .map(response ->
                        {
                            response.put("message", "Este recurso se encuentra disponible");
                            return response;
                        });
    }

    public Mono<Map<String, Object>> lend(String id)
    {
        return findById(id).map(resourceMapper::fromDTO)
                .flatMap(resource ->
                {
                    if (resource.isLent())
                        return resourceLentResponse(resource);

                    resource.lend();
                    return getSucessfulLentResponse(resource);
                });
    }

    private Mono<HashMap<String, Object>> getSucessfulLentResponse(Resource resource) {
        return Mono.just(resource)
                .map(resourceMapper::fromEntity)
                .flatMap(resourceDTO ->
                        update(resourceDTO).map(resourceUpdated ->
                    {
                        var response = new HashMap<String, Object>();
                        response.put("message", "Recurso prestado exitosamente");
                        response.put("resource", resourceUpdated);
                        return  response;
                    })
                );
    }

    private Mono<Map<String, Object>> resourceLentResponse(Resource resource)
    {
        return Mono.just(new HashMap<String, Object>())
                .map(response ->
                {
                    response.put("message", "Este recurso no se encuentra disponible para prestar");
                    response.put("resource", resourceMapper.fromEntity(resource));
                    return response;
                });
    }

    public Mono<ResourceDTO> findById(String id)
    {
        resourceRepository.findById(id).hasElement().subscribe(response ->
        {
           if (!response)
               throw new RuntimeException("Este recurso no ha sido encontrado");
        });

        return resourceRepository.findById(id)
                .map(resourceMapper::fromEntity);
    }

    public Mono<ResourceDTO> update(ResourceDTO resourceDTO)throws RuntimeException
    {
        return  Mono.just(resourceDTO)
                .map(resourceMapper::fromDTO)
                .flatMap(resource ->
                    findById(resourceDTO.getId())
                            .flatMap(resourceDTOFound -> resourceRepository.save(resource))
                )
                .map(resourceMapper::fromEntity);
    }

}
