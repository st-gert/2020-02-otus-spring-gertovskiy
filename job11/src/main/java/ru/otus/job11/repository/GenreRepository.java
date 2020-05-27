package ru.otus.job11.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.job11.model.Genre;

/**
 * Repository Литературные жанры.
 */
public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    Flux<Genre> findAllByOrderByGenreName();

}


