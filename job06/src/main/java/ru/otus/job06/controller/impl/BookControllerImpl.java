package ru.otus.job06.controller.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job06.controller.BookController;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Genre;
import ru.otus.job06.service.BookService;

import java.util.List;

@Service
public class BookControllerImpl implements BookController {

    private final BookService service;

    private final AuthorUtil authorUtil;
    private final ResultUtil resultUtil;

    public BookControllerImpl(BookService service, AuthorUtil authorUtil, ResultUtil resultUtil) {
        this.service = service;
        this.authorUtil = authorUtil;
        this.resultUtil = resultUtil;
    }


    @Override
    public Pair<List<Book>, String> getBookList() {
        try {
            return resultUtil.handleList(service.getBookList());
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<List<Book>, String> getBookListByGenre(String genre) {
        try {
            return resultUtil.handleList(service.getBookListByGenre(genre));
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<List<Book>, String> getBookListByAuthor(String authorLastName) {
        try {
            return resultUtil.handleList(service.getBookListByAuthor(authorLastName));
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<Long, String> addBook(String title, String genre, String authors) {
        Book book = new Book(null, title, new Genre(null, genre), authorUtil.createAuthorList(authors));
        try {
            return Pair.of(service.addBook(book), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateBook(Long bookId, String title, String genre, String authors) {
        Book book = new Book(bookId, title, new Genre(null, genre), authorUtil.createAuthorList(authors));
        try {
            service.updateBook(book);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteBook(Long bookId) {
        try {
            service.deleteBook(bookId);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
