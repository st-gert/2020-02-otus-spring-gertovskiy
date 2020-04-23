package ru.otus.job08.controller.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job08.controller.BookController;
import ru.otus.job08.model.Book;
import ru.otus.job08.model.Genre;
import ru.otus.job08.service.BookService;

import java.util.Comparator;
import java.util.List;

@Service
public class BookControllerImpl implements BookController {

    private final BookService service;

    private final ResultUtil resultUtil;

    public BookControllerImpl(BookService service, ResultUtil resultUtil) {
        this.service = service;
        this.resultUtil = resultUtil;
    }


    @Override
    public Pair<List<Book>, String> getBookList() {
        try {
            List<Book> bookList = service.getBookList();
            sortByGenreAuthorTitle(bookList);
            return resultUtil.handleList(bookList);
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<List<Book>, String> getBookListByGenre(String genre) {
        try {
            List<Book> bookList = service.getBookListByGenre(genre);
            sortByGenreAuthorTitle(bookList);
            return resultUtil.handleList(bookList);
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<List<Book>, String> getBookListByAuthor(String authorLastName) {
        try {
            List<Book> bookList = service.getBookListByAuthor(authorLastName);
            sortByGenreAuthorTitle(bookList);
            return resultUtil.handleList(bookList);
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<String, String> addBook(String title, String genre, String authors) {
        Book book = new Book(title, new Genre(genre), AuthorUtil.createAuthorList(authors));
        try {
            return Pair.of(service.addBook(book), null);
        } catch (Exception e) {
            return Pair.<String, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateBook(String bookId, String title, String genre, String authors) {
        Book book = new Book(title, new Genre(genre), AuthorUtil.createAuthorList(authors));
        book.setId(bookId);
        try {
            service.updateBook(book);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteBook(String bookId) {
        try {
            service.deleteBook(bookId);
            return null;
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    private void sortByGenreAuthorTitle(List<Book> bookList) {
        bookList.sort(Comparator
                .<Book, String>comparing(b -> b.getGenre().getGenreName())
                .thenComparing(b -> b.getAuthors().get(0).getLastName())
                .thenComparing(Book::getTitle)
        );
    }

}
