package ru.otus.job06.repository;

import ru.otus.job06.model.Book;

import java.util.List;

/**
 * Repository Книги.
 */
public interface BookRepository {

    List<Book> getBookList();

    Book getBookById(Long bookId);

    long addBook(Book book);

    void updateBook(Book book);

    void deleteBook(Book book);

}
