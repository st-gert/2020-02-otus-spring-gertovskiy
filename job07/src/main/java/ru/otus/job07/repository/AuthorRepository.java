package ru.otus.job07.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job07.model.Author;

/**
 * Repository Авторы.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Используется в BookService для заполнения Book
    Author getByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

}
