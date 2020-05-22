package ru.otus.job10.service;

import ru.otus.job10.model.Genre;
import ru.otus.job10.model.dto.GenreDto;

import java.util.List;

public interface GenreService {

    List<GenreDto> getGenreList();

    GenreDto getGenreById(long id);

    void addGenre(Genre genre);

    void updateGenre(Genre genre);

    void deleteGenre(Long genreId);

}


