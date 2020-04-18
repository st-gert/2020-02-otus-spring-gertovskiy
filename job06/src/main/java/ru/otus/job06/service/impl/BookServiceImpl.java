package ru.otus.job06.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.exception.ApplDbNoDataFoundtException;
import ru.otus.job06.model.Author;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Genre;
import ru.otus.job06.model.Review;
import ru.otus.job06.repository.AuthorRepository;
import ru.otus.job06.repository.BookRepository;
import ru.otus.job06.repository.GenreRepository;
import ru.otus.job06.service.BookService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookServiceImpl(BookRepository repository, AuthorRepository authorRepository,
                           GenreRepository genreRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public List<Book> getBookList() {
        return repository.getBookList()
                .stream()
                .distinct()
                .peek(this::sortAuthorsAndReviews)
                .collect(Collectors.toList())
                ;
    }

    @Override
    @Transactional
    public List<Book> getBookListByGenre(String genreName) {
        Genre genre = genreRepository.getGenreByName(genreName);
        if (genre == null || genre.getBooks().isEmpty()) {
            throw new ApplDbNoDataFoundtException();
        }
        List<Book> books = genre.getBooks();
        books.forEach(this::sortAuthorsAndReviews);
        return books;
    }

    @Override
    @Transactional
    public List<Book> getBookListByAuthor(String authorLastName) {
        List<Author> authors = authorRepository.getAuthorList();
        List<Book> books = authors
                .stream()
                .filter(x -> x.getLastName().equalsIgnoreCase(authorLastName))
                .flatMap(x -> x.getBooks().stream())
                .distinct()
                .peek(this::sortAuthorsAndReviews)
                .collect(Collectors.toList());
        if (books.isEmpty()) {
            throw new ApplDbNoDataFoundtException();
        }
        return books;
    }

    @Override
    @Transactional
    public long addBook(Book book) {
        obtainAuthorAndGenreId(book);
        return repository.addBook(book);
    }

    @Override
    @Transactional
    public void updateBook(Book book) {
        Book currentBook = repository.getBookById(book.getBookId());
        if (currentBook == null) {
            throw new ApplDbNoDataFoundtException();
        }
        obtainAuthorAndGenreId(book);
        repository.updateBook(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Book book = repository.getBookById(bookId);
        if (book == null) {
            throw new ApplDbNoDataFoundtException();
        }
        repository.deleteBook(book);
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

    private void sortAuthorsAndReviews(Book book) {
        book.getAuthors().sort(Comparator
                .comparing(Author::getLastName)
                .thenComparing(Author::getFirstName)
        );
        book.getReviews().sort(Comparator
                .comparing(Review::getReviewId)
        );
    }

}
