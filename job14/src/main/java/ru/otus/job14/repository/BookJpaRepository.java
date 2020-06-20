package ru.otus.job14.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job14.model.jpa.BookJpa;

import java.util.UUID;

/**
 * Repository Книги.
 */
public interface BookJpaRepository extends JpaRepository<BookJpa, UUID> {
}
