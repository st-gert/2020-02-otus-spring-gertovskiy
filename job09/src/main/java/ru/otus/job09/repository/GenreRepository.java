package ru.otus.job09.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job09.model.Genre;

/**
 * Repository Литературные жанры.
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {
}


