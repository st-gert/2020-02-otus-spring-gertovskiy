package ru.otus.job06.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.otus.job06.exception.ApplDbConstraintException;
import ru.otus.job06.exception.ApplDbNoDataFoundtException;
import ru.otus.job06.model.Author;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Genre;
import ru.otus.job06.repository.AuthorRepository;
import ru.otus.job06.repository.BookRepository;
import ru.otus.job06.repository.GenreRepository;
import ru.otus.job06.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookServiceImpl(BookRepository repository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Book> getBookList() {
        return distinct(repository.getBookList());
    }

    @Override
    public List<Book> getBookListByGenre(String genre) {
        return distinct(repository.getBookListByGenre(genre));
    }

    @Override
    public List<Book> getBookListByAuthor(String authorLastName) {
        return distinct(repository.getBookListByAuthor(authorLastName));
    }

    @Override
    public Optional<Book> getBookById(Long bookId) {
        return Optional.ofNullable( repository.getBookById(bookId) );
    }

    @Override
    public long addBook(Book book) {
        obtainAuthorAndGenreId(book);
        return repository.addBook(book);
    }

    @Override
    public void updateBook(Book book) {
        Book currentBook = repository.getBookById(book.getBookId());
        if (currentBook == null) {
            throw new ApplDbNoDataFoundtException();
        }
        try {
            obtainAuthorAndGenreId(book);
            repository.updateBook(book);
        } catch (DataIntegrityViolationException e) {
            throw new ApplDbConstraintException();
        }
    }

    @Override
    public void deleteBook(Long bookId) {
        Book book = repository.getBookById(bookId);
        if (book == null) {
            throw new ApplDbNoDataFoundtException();
        }
        try {
            repository.deleteBook(book);
        } catch (DataIntegrityViolationException e) {
            throw new ApplDbConstraintException();
        }
    }

    private void obtainAuthorAndGenreId(Book book) {
        Genre genre = book.getGenre();
        Genre foundGenre = genreRepository.getGenreByName(genre.getGenreName());
        if (foundGenre != null) {
            genre.setGenreId(foundGenre.getGenreId());
        } else {
            genre.setGenreId(genreRepository.addGenre(genre));
        }
        for (Author author : book.getAuthors()) {
            Author foundAuthor = authorRepository.getAuthorByName(author);
            if (foundAuthor != null) {
                author.setAuthorId(foundAuthor.getAuthorId());
            } else {
                author.setAuthorId(authorRepository.addAuthor(author));
            }
        }
    }

    private List<Book> distinct(List<Book> books) {
        return books
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
