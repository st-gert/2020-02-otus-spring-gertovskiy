package ru.otus.job10.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job10.exception.ApplDbNoDataFoundException;
import ru.otus.job10.model.Author;
import ru.otus.job10.model.Book;
import ru.otus.job10.model.Genre;
import ru.otus.job10.model.dto.BookDto;
import ru.otus.job10.model.dto.BookInput;
import ru.otus.job10.repository.AuthorRepository;
import ru.otus.job10.repository.BookRepository;
import ru.otus.job10.repository.GenreRepository;
import ru.otus.job10.service.BookService;

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
    public List<BookDto> getBookList() {
        return repository.findAll()
                .stream()
                .map(BookDto::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDto> getBookListByGenre(long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(ApplDbNoDataFoundException::new);
        if (genre.getBooks().isEmpty()) {
            throw new ApplDbNoDataFoundException();
        }
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
        Genre genre = genreRepository.findById(bookInput.getGenreId())
                .orElseThrow(ApplDbNoDataFoundException::new);
        List<Author> authors = authorRepository.findByAuthorIdIn(bookInput.getAuthorsIds());
        Book book = new Book(null, bookInput.getTitle(), genre, authors);
        repository.save(book);
    }

    @Override
    @Transactional
    public void updateBook(BookInput bookInput) {
        Book book = repository.findById(bookInput.getBookId())
                .orElseThrow(ApplDbNoDataFoundException::new);
        Genre genre = genreRepository.findById(bookInput.getGenreId())
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
