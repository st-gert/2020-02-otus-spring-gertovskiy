package ru.otus.job16.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.job16.model.Book;

import java.util.List;

/**
 * Repository Книги.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph("book-entity-graph")
    @Override
    List<Book> findAll();

    // Проверка доступности БД
    @Query(value = "select count(*) from book limit 1", nativeQuery = true)
    int testBookEmpty();
}
