package co.com.sofka.libraryapireactive.repositories;


import co.com.sofka.libraryapireactive.collections.Resource;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResourceRepository extends ReactiveMongoRepository<Resource, String> {
    List<Resource> findByType(String type);
    List<Resource> findBySubjectArea(String type);
    List<Resource> findByTypeAndSubjectArea(String type, String subjectArea);
}