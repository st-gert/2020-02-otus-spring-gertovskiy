package ru.otus.job06.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job06.model.Author;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Genre;
import ru.otus.job06.repository.AuthorRepository;
import ru.otus.job06.repository.BookRepository;
import ru.otus.job06.repository.GenreRepository;
import ru.otus.job06.service.BookController;

import java.util.List;
import java.util.Optional;

@Service
public class BookControllerImpl implements BookController {

    private final BookRepository repository;
    private final AuthorRepository repositoryAuthor;
    private final GenreRepository repositoryGenre;

    private final AuthorUtil authorUtil;
    private final ResultUtil resultUtil;

    public BookControllerImpl(BookRepository repository, AuthorRepository repositoryAuthor, GenreRepository repositoryGenre,
                              AuthorUtil authorUtil, ResultUtil resultUtil) {
        this.repository = repository;
        this.repositoryAuthor = repositoryAuthor;
        this.repositoryGenre = repositoryGenre;
        this.authorUtil = authorUtil;
        this.resultUtil = resultUtil;
    }


    @Override
    public Pair<List<Book>, String> getBookList() {
        try {
            return resultUtil.handleList(repository.getBookList());
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<List<Book>, String> getBookListByGenre(String genre) {
        try {
            return resultUtil.handleList(repository.getBookListByGenre(genre));
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<List<Book>, String> getBookListByAuthor(String authorLastName) {
        try {
            return resultUtil.handleList(repository.getBookListByAuthor(authorLastName));
        } catch (Exception e) {
            return Pair.<List<Book>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<Long, String> addBook(String title, String genre, String authors) {
        Book book = new Book(null, title, new Genre(null, genre), authorUtil.createAuthorList(authors));
        obtainAuthorAndGenreId(book);
        try {
            return Pair.of(repository.addBook(book), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String deleteBook(Long bookId) {
        try {
            return resultUtil.handleInt(repository.deleteBook(bookId));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public Pair<Long, String> updateBook(Long bookId, String title, String genre, String authors) {
        Book book = new Book(bookId, title, new Genre(null, genre), authorUtil.createAuthorList(authors));
        obtainAuthorAndGenreId(book);
        try {
            Optional<Book> newBook = repository.updateBook(book);
            return newBook
                    .map(x -> Pair.<Long, String>of(x.getBookId().equals(bookId) ? null : x.getBookId(), null))
                    .orElse(Pair.<Long, String>of(null, "Данные не найдены"));
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    private void obtainAuthorAndGenreId(Book book) {
        Genre genre = book.getGenre();
        long genreId = repositoryGenre.getGenreByName(genre.getGenreName())
                .map(Genre::getGenreId)
                .orElse(repositoryGenre.addGenre(book.getGenre()));
        genre.setGenreId(genreId);
        for (Author author : book.getAuthors()) {
            long authorId = repositoryAuthor.getAuthorByName(author)
                    .map(Author::getAuthorId)
                    .orElse(repositoryAuthor.addAuthor(author));
            author.setAuthorId(authorId);
        }
    }

}
