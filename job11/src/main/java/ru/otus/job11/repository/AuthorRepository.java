package ru.otus.job11.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.job11.model.Author;

/**
 * Repository Авторы.
 */
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    Flux<Author> findAllByOrderByLastNameAscFirstNameAsc();
}
