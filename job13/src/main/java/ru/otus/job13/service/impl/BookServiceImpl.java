package ru.otus.job13.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job13.exception.ApplDbNoDataFoundException;
import ru.otus.job13.model.Author;
import ru.otus.job13.model.Book;
import ru.otus.job13.model.Genre;
import ru.otus.job13.model.dto.BookDto;
import ru.otus.job13.repository.AuthorRepository;
import ru.otus.job13.repository.BookRepository;
import ru.otus.job13.repository.GenreRepository;
import ru.otus.job13.service.BookService;

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
                .map(BookDto::new)
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
                .map(BookDto::new)
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
                .map(BookDto::new)
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
        return new BookDto(book, true);
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
    public void addBook(String title, Long genreId, List<Long> authorsIds) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(ApplDbNoDataFoundException::new);
        List<Author> authors = authorRepository.findByAuthorIdIn(authorsIds);
        Book book = new Book(null, title, genre, authors);
        repository.save(book);
    }

    @Override
    @Transactional
    public void updateBook(long bookId, String title, Long genreId, List<Long> authorsIds) {
        Book book = repository.findById(bookId)
                .orElseThrow(ApplDbNoDataFoundException::new);
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(ApplDbNoDataFoundException::new);
        List<Author> authors = authorRepository.findByAuthorIdIn(authorsIds);
        book.setTitle(title);
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
