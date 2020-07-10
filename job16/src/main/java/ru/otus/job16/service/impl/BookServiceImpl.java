package ru.otus.job16.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job16.controller.repository.GenreControllerRepository;
import ru.otus.job16.exception.ApplDbNoDataFoundException;
import ru.otus.job16.model.Author;
import ru.otus.job16.model.Book;
import ru.otus.job16.model.Genre;
import ru.otus.job16.model.dto.BookDto;
import ru.otus.job16.model.dto.BookInput;
import ru.otus.job16.repository.AuthorRepository;
import ru.otus.job16.repository.BookRepository;
import ru.otus.job16.service.BookService;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final GenreControllerRepository genreRepository;

    public BookServiceImpl(BookRepository repository, AuthorRepository authorRepository,
                           GenreControllerRepository genreRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<BookDto> getBookList() {
        List<BookDto> books = repository.findAll()
                .stream()
                .map(BookDto::of)
                .collect(Collectors.toList());
        LOGGER.info("Список всех книг, книг {}", books.size());
        return books;
    }

    @Override
    @Transactional
    public List<BookDto> getBookListByGenre(String genreName) {
        Genre genre = genreRepository.findByGenreName(genreName)
                .orElseThrow(ApplDbNoDataFoundException::new);
        if (genre.getBooks().isEmpty()) {
            throw new ApplDbNoDataFoundException();
        }
        LOGGER.info("Список книг по жанру {}, книг {}", genre.getGenreName(), genre.getBooks().size());
        return genre.getBooks()
                .stream()
                .map(BookDto::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDto> getBookListByAuthors(List<Long> authorsIds) {
        List<Author> authors = authorRepository.findByAuthorIdIn(authorsIds);
        List<BookDto> books = authors
                .stream()
                .flatMap(x -> x.getBooks().stream())
                .distinct()
                .map(BookDto::of)
                .collect(Collectors.toList());
        LOGGER.info("Список книг по автору {}, книг {}",
                authors.stream().map(Author::getFullName).collect(Collectors.joining(", ")),
                books.size());
        if (books.isEmpty()) {
            throw new ApplDbNoDataFoundException();
        }
        return books;
    }

    @Override
    @Transactional
    public BookDto getBookWithReview(long bookId) {
        Book book = repository.findById(bookId)
                .orElseThrow(ApplDbNoDataFoundException::new);
        return BookDto.bookWithReviews(book);
    }

    @Override
    @Transactional
    public Book getBookById(long bookId) {
        Book book = repository.findById(bookId)
                .orElseThrow(ApplDbNoDataFoundException::new);
        book.getAuthors().sort(Comparator
                .comparing(Author::getLastName)
                .thenComparing(Author::getFirstName));
        return book;
    }

    @Override
    @Transactional
    public void addBook(BookInput bookInput) {
        Genre genre = genreRepository.findByGenreName(bookInput.getGenreName())
                .orElseThrow(ApplDbNoDataFoundException::new);
        List<Author> authors = authorRepository.findByAuthorIdIn(bookInput.getAuthorsIds());
        Book book = new Book(null, bookInput.getTitle(), genre, authors);
        Book savedBook = repository.save(book);
        LOGGER.info("Новая книга ID {}", savedBook.getBookId());
    }

    @Override
    @Transactional
    public void updateBook(BookInput bookInput) {
        Book book = repository.findById(bookInput.getBookId())
                .orElseThrow(ApplDbNoDataFoundException::new);
        Genre genre = genreRepository.findByGenreName(bookInput.getGenreName())
                .orElseThrow(ApplDbNoDataFoundException::new);
        List<Author> authors = authorRepository.findByAuthorIdIn(bookInput.getAuthorsIds());
        book.setTitle(bookInput.getTitle());
        book.setGenre(genre);
        book.setAuthors(authors);
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

}
