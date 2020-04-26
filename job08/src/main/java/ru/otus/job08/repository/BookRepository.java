package ru.otus.job08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.job08.model.Book;

/**
 * Repository Книги.
 */
public interface BookRepository extends MongoRepository<Book, String> {
}
