package ru.otus.job16.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job16.model.Author;

import java.util.List;

/**
 * Repository Авторы.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Для получения списка книг по выбранным авторам,
    // и для добавления / корректировки книги
    List<Author> findByAuthorIdIn(List<Long> authorId);

}
