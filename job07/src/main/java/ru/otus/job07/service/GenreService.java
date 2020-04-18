package ru.otus.job07.service;

import ru.otus.job07.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<Genre> getGenreList();

    Optional<Genre> getGenreById(long id);

    long addGenre(Genre genre);

    void updateGenre(Genre genre);

    void deleteGenre(Long genreId);

}


