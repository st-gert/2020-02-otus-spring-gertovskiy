package ru.otus.job06.repository;

import ru.otus.job06.model.Genre;

import java.util.List;
import java.util.Optional;

/**
 * Repository Литературные жанры.
 */
public interface GenreRepository {

    List<Genre> getGenreList();
    Optional<Genre> getGenreById(Long id);
    Optional<Genre> getGenreByName(String genreName);

    long addGenre(Genre genre);

    int updateGenre(Genre genre);

    int deleteGenre(Long genreId);

}


