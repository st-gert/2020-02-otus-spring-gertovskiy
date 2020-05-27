package ru.otus.job11.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.job11.model.Book;

/**
 * Repository Книги.
 */
public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findByTitle(String title);
}
