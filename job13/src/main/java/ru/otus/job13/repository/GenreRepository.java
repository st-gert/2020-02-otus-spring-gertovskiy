package ru.otus.job13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job13.model.Genre;

/**
 * Repository Литературные жанры.
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {
}


