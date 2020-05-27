package ru.otus.job11.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.job11.model.Author;
import ru.otus.job11.model.Book;
import ru.otus.job11.model.Genre;
import ru.otus.job11.model.dto.BookDto;
import ru.otus.job11.model.dto.BookInput;
import ru.otus.job11.repository.AuthorRepository;
import ru.otus.job11.repository.BookRepository;
import ru.otus.job11.repository.GenreRepository;
import ru.otus.job11.repository.ReviewRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookRepository repository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final ReviewRepository reviewRepository;

    public BookController(BookRepository repository, GenreRepository genreRepository,
                          AuthorRepository authorRepository, ReviewRepository reviewRepository) {
        this.repository = repository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping
    public Mono<List<BookDto>> getAllBooks() {
        return repository.findAll()
                .collectList()
                .map(this::convertBookList)
                ;
    }

    @GetMapping("/genre/{genreId}")
    public Mono<List<BookDto>> getBooksByGenre(@PathVariable("genreId") String genreId) {
        return genreRepository.findById(genreId)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр ID " + genreId + " не найден")))
                .flatMapIterable(Genre::getBooks)
                .collectList()
                .map(this::convertBookList)
                ;
    }

    @GetMapping("/authors/{authorIds}")
    public Mono<List<BookDto>> getBooksByAuthors(@PathVariable("authorIds") List<String> authorIds) {
        return Flux.fromIterable(authorIds)
                .flatMap(authorRepository::findById)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Авторы не найдены")))
                .flatMapIterable(Author::getBooks)
                .distinct()
                .collectList()
                .map(this::convertBookList)
                ;
    }

    private List<BookDto> convertBookList(List<Book> list) {
        return list
                .stream()
                .map(BookDto::of)
                .sorted(Comparator
                        .comparing(BookDto::getGenreName)
                        .thenComparing(BookDto::getFirstAuthor)
                        .thenComparing(BookDto::getTitle))
                .collect(Collectors.toList())
                ;
    }

    @GetMapping("/{id}")
    public Mono<BookDto> getBookById(@PathVariable("id") String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга ID " + id + " не найдена")))
                .map(BookDto::of)
                ;
    }

    @PostMapping
    public Mono<BookDto> addBook(@RequestBody BookInput bookInput) {
        if (bookInput.getBookId() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Невозможно добавить книгу с имеющимся ID");
        }
        Book book = new Book();
        book.update(bookInput.getTitle(), bookInput.getGenreId(), bookInput.getAuthorsIds());
        return repository.insert(book)
                .map(BookDto::of)
                ;
    }

    @PutMapping
    public Mono<BookDto> updateBook(@RequestBody BookInput bookInput) {
        return repository.findById(bookInput.getBookId())
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Книга ID " + bookInput.getBookId() + " не найдена")))
                .map(b -> {
                            b.update(bookInput.getTitle(), bookInput.getGenreId(), bookInput.getAuthorsIds());
                            return b;
                        }
                )
                .flatMap(repository::save)
                .map(BookDto::of)
                ;
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteBook(@PathVariable("id") String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Книга ID " + id + " не найдена")))
                .map(book -> {      // удаляем отзывы и связи с жанром и авторами
                    book.getReviews()
                            .forEach(reviewRepository::delete);
                    book.getGenre().getBooks().remove(book);
                    genreRepository.save(book.getGenre());
                    book.getAuthors()
                            .forEach(author -> {
                                author.getBooks().remove(book);
                                authorRepository.save(author);
                            });
                    return book;
                })
                .flatMap(repository::delete)
                ;
    }

}
