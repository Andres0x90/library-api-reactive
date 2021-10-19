package co.com.sofka.libraryapireactive.repositories;


import co.com.sofka.libraryapireactive.collections.Resource;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
@Repository
public interface ResourceRepository extends ReactiveMongoRepository<Resource, String> {
    Flux<Resource> findByType(String type);
    Flux<Resource> findBySubjectArea(String type);
    Flux<Resource> findByTypeAndSubjectArea(String type, String subjectArea);
}