package io.learnk8s.knote;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotesRepository extends MongoRepository<Application.Note, String> {

}
