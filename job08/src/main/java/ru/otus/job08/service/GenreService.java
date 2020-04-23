package ru.otus.job08.service;

import ru.otus.job08.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<Genre> getGenreList();

    Optional<Genre> getGenreById(String id);

    String addGenre(Genre genre);

    void updateGenre(String genreId, String genreName);

    void deleteGenre(String genreId);

}


