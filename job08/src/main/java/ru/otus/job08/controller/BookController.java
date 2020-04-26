package ru.otus.job08.controller;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.job08.model.Book;

import java.util.List;

/**
 * Контроллер запросов по работе с книгами.
 */
public interface BookController {

    Pair<List<Book>, String> getBookList();

    Pair<List<Book>, String> getBookListByGenre(String genre);

    Pair<List<Book>, String> getBookListByAuthor(String authorLastName);

    Pair<String, String> addBook(String title, String genre, String authors);

    String updateBook(String bookId, String title, String genre, String authors);

    String deleteBook(String bookId);
}
