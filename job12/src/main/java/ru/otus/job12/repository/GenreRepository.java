package ru.otus.job12.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job12.model.Genre;

/**
 * Repository Литературные жанры.
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {
}


