package ru.otus.job07.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.job07.model.Genre;

/**
 * Repository Литературные жанры.
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {

    // Используется в BookService для заполнения Book
    Genre findByGenreNameIgnoreCase(String genreName);

}


