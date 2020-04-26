package ru.otus.job08.service;

import ru.otus.job08.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getBookList();
    List<Book> getBookListByGenre(String genreName);
    List<Book> getBookListByAuthor(String authorLastName);

    String addBook(Book book);

    void updateBook(Book book);

    void deleteBook(String bookId);

}
