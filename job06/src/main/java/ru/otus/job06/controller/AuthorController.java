package ru.otus.job06.controller;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.job06.model.Author;

import java.util.List;

/**
 * Контроллер запросов по авторам.
 */
public interface AuthorController {

    Pair<List<Author>, String> getAuthorList();

    Pair<Long, String> addAuthor(String author);

    String deleteAuthor(Long authorId);

    String updateAuthor(Long authorId, String author);

}
