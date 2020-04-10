package ru.otus.job06.service;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.job06.model.Book;

import java.util.List;

/**
 * Контроллер запросов по работе с книгами.
 */
public interface BookController {

    Pair<List<Book>, String> getBookList();

    Pair<List<Book>, String> getBookListByGenre(String genre);

    Pair<List<Book>, String> getBookListByAuthor(String authorLastName);

    Pair<Long, String> addBook(String title, String genre, String authors);

    String deleteBook(Long bookId);

    Pair<Long, String> updateBook(Long bookId, String title, String genre, String authors);
}
