package ru.otus.job05.service;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.job05.model.Genre;

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
