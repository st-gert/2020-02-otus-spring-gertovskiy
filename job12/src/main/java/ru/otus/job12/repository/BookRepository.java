package ru.otus.job12.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job12.model.Book;

import java.util.List;

/**
 * Repository Книги.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    @SuppressWarnings("NullableProblems")
    @EntityGraph("book-entity-graph")
    @Override
    List<Book> findAll();

}
