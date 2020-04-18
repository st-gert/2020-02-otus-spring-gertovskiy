package ru.otus.job07.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job07.exception.ApplDbNoDataFoundException;
import ru.otus.job07.model.Author;
import ru.otus.job07.model.Book;
import ru.otus.job07.model.Genre;
import ru.otus.job07.model.Review;
import ru.otus.job07.repository.AuthorRepository;
import ru.otus.job07.repository.BookRepository;
import ru.otus.job07.repository.GenreRepository;
import ru.otus.job07.service.BookService;

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
        return repository.findAll()
                .stream()
                .distinct()
                .peek(this::sortAuthorsAndReviews)
                .collect(Collectors.toList())
                ;
    }

    @Override
    @Transactional
    public List<Book> getBookListByGenre(String genreName) {
        Genre genre = genreRepository.findByGenreNameIgnoreCase(genreName);
        if (genre == null || genre.getBooks().isEmpty()) {
            throw new ApplDbNoDataFoundException();
        }
        List<Book> books = genre.getBooks();
        books.forEach(this::sortAuthorsAndReviews);
        return books;
    }

    @Override
    @Transactional
    public List<Book> getBookListByAuthor(String authorLastName) {
        List<Author> authors = authorRepository.findAll();
        List<Book> books = authors
                .stream()
                .filter(x -> x.getLastName().equalsIgnoreCase(authorLastName))
                .flatMap(x -> x.getBooks().stream())
                .distinct()
                .peek(this::sortAuthorsAndReviews)
                .collect(Collectors.toList());
        if (books.isEmpty()) {
            throw new ApplDbNoDataFoundException();
        }
        return books;
    }

    @Override
    @Transactional
    public long addBook(Book book) {
        obtainAuthorAndGenreId(book);
        return repository.save(book).getBookId();
    }

    @Override
    @Transactional
    public void updateBook(Book book) {
        if (!repository.existsById(book.getBookId())) {
            throw new ApplDbNoDataFoundException();
        }
        obtainAuthorAndGenreId(book);
        repository.save(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        if (!repository.existsById(bookId)) {
            throw new ApplDbNoDataFoundException();
        }
        repository.deleteById(bookId);
    }

    private void obtainAuthorAndGenreId(Book book) {
        Genre genre = book.getGenre();
        Genre foundGenre = genreRepository.findByGenreNameIgnoreCase(genre.getGenreName());
        if (foundGenre != null) {
            genre.setGenreId(foundGenre.getGenreId());
        } else {
            genre.setGenreId(genreRepository.save(genre).getGenreId());
        }
        for (Author author : book.getAuthors()) {
            Author foundAuthor = authorRepository.getByFirstNameIgnoreCaseAndLastNameIgnoreCase(author.getFirstName(), author.getLastName());
            if (foundAuthor != null) {
                author.setAuthorId(foundAuthor.getAuthorId());
            } else {
                author.setAuthorId(authorRepository.save(author).getAuthorId());
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
