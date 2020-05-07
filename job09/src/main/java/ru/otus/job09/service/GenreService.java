package ru.otus.job09.service;

import ru.otus.job09.model.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> getGenreList();

    Genre getGenreById(long id);

    void addGenre(Genre genre);

    void updateGenre(Genre genre);

    void deleteGenre(Long genreId);

}


