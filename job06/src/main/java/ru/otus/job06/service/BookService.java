package ru.otus.job06.service;

import ru.otus.job06.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getBookList();
    List<Book> getBookListByGenre(String genreName);
    List<Book> getBookListByAuthor(String authorLastName);

    long addBook(Book book);

    void updateBook(Book book);

    void deleteBook(Long bookId);

}
