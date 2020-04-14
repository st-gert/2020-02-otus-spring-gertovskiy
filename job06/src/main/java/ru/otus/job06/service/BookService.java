package ru.otus.job06.service;

import ru.otus.job06.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> getBookList();
    List<Book> getBookListByGenre(String genre);
    List<Book> getBookListByAuthor(String authorLastName);

    Optional<Book> getBookById(Long bookId);

    long addBook(Book book);

    void updateBook(Book book);

    void deleteBook(Long bookId);

}
