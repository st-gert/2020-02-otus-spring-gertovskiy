package ru.otus.job08.controller;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.job08.model.Author;

import java.util.List;

/**
 * Контроллер запросов по авторам.
 */
public interface AuthorController {

    Pair<List<Author>, String> getAuthorList();

    Pair<String, String> addAuthor(String author);

    String deleteAuthor(String authorId);

    String updateAuthor(String authorId, String author);

}
