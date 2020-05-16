package ru.otus.job10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job10.model.Genre;

/**
 * Repository Литературные жанры.
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {
}


