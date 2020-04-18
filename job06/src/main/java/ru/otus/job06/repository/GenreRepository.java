package ru.otus.job06.repository;

import ru.otus.job06.model.Genre;

import java.util.List;

/**
 * Repository Литературные жанры.
 */
public interface GenreRepository {

    List<Genre> getGenreList();

    Genre getGenreById(Long id);

    // Используется в BookService для заполнения Book
    Genre getGenreByName(String genreName);

    long addGenre(Genre genre);

    void updateGenre(Genre genre);

    void deleteGenre(Genre genre);

}


