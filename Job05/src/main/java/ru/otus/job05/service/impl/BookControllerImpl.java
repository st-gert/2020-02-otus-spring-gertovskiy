package ru.otus.job05.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job05.dao.AuthorDao;
import ru.otus.job05.dao.BookDao;
import ru.otus.job05.dao.GenreDao;
import ru.otus.job05.model.Author;
import ru.otus.job05.model.Book;
import ru.otus.job05.model.Genre;
import ru.otus.job05.service.BookController;

import java.util.List;
import java.util.Optional;

@Service
public class BookControllerImpl implements BookController {

    private final BookDao dao;
    private final AuthorDao daoAuthor;
    private final GenreDao daoGenre;

    private final AuthorUtil authorUtil;
    private final ResultUtil resultUtil;

    public BookControllerImpl(BookDao dao, AuthorDao daoAuthor, GenreDao daoGenre,
                              AuthorUtil authorUtil, ResultUtil resultUtil) {
        this.dao = dao;
        this.daoAuthor = daoAuthor;
        this.daoGenre = daoGenre;
        this.authorUtil = authorUtil;
        this.resultUtil = resultUtil;
    }


    @Override
    public Pair<List<Book>, String> getBookList() {
        try {
            return resultUtil.handleList(dao.getBookList());
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<List<Book>, String> getBookListByGenre(String genre) {
        try {
            return resultUtil.handleList(dao.getBookListByGenre(genre));
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<List<Book>, String> getBookListByAuthor(String authorLastName) {
        try {
            return resultUtil.handleList(dao.getBookListByAuthor(authorLastName));
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<Long, String> addBook(String title, String genre, String authors) {
        Book book = new Book(null, title, new Genre(null, genre), authorUtil.createAuthorList(authors));
        obtainAuthorAndGenreId(book);
        try {
            return Pair.of(dao.addBook(book), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String deleteBook(Long bookId) {
        try {
            return resultUtil.handleInt(dao.deleteBook(bookId));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public Pair<Long, String> updateBook(Long bookId, String title, String genre, String authors) {
        Book book = new Book(bookId, title, new Genre(null, genre), authorUtil.createAuthorList(authors));
        obtainAuthorAndGenreId(book);
        try {
            Optional<Book> newBook = dao.updateBook(book);
            return newBook
                    .map(x -> Pair.<Long, String>of(x.getBookId().equals(bookId) ? null : x.getBookId(), null))
                    .orElse(Pair.<Long, String>of(null, "Данные не найдены"));
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    private void obtainAuthorAndGenreId(Book book) {
        Genre genre = book.getGenre();
        long genreId = daoGenre.getGenreByName(genre.getGenreName())
                .map(Genre::getGenreId)
                .orElse(daoGenre.addGenre(book.getGenre()));
        genre.setGenreId(genreId);
        for (Author author : book.getAuthors()) {
            long authorId = daoAuthor.getAuthorByName(author)
                    .map(Author::getAuthorId)
                    .orElse(daoAuthor.addAuthor(author));
            author.setAuthorId(authorId);
        }
    }

}
