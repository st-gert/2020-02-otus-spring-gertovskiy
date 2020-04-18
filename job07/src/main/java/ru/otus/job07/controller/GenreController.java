package ru.otus.job07.controller;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.job07.model.Genre;

import java.util.List;

/**
 * Контроллер запросов по лит. жанрам.
 */
public interface GenreController {

    Pair<List<Genre>, String> getGenreList();

    Pair<Long, String> addGenre(String genre);

    String deleteGenre(Long genreId);

    String updateGenre(Long genreId, String genre);

}
