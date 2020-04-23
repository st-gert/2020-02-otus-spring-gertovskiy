package ru.otus.job08.controller;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.job08.model.Genre;

import java.util.List;

/**
 * Контроллер запросов по лит. жанрам.
 */
public interface GenreController {

    Pair<List<Genre>, String> getGenreList();

    Pair<String, String> addGenre(String genre);

    String deleteGenre(String genreId);

    String updateGenre(String genreId, String genre);

}
