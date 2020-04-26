package ru.otus.job08.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.job08.model.Author;

/**
 * Repository Авторы.
 */
public interface AuthorRepository extends MongoRepository<Author, String> {

    // Используется в BookService для заполнения Book
    Author getByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

}
