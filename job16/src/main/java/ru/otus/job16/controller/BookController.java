package ru.otus.job16.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.otus.job16.exception.ApplDbNoDataFoundException;
import ru.otus.job16.model.dto.BookDto;
import ru.otus.job16.model.dto.BookInput;
import ru.otus.job16.service.BookService;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookDto> getAllBooks() {
        LOGGER.info("Список всех книг");
        List<BookDto> list = service.getBookList();
        sortBooks(list);
        return list;
    }

    @GetMapping("/genre/{genreName}")
    public List<BookDto> getBooksByGenre(@PathVariable("genreName") String genreName) {
        LOGGER.info("Список книг по жанру {}", genreName);
        try {
            List<BookDto> list = service.getBookListByGenre(genreName);
            sortBooks(list);
            return list;
        } catch (ApplDbNoDataFoundException e) {
            LOGGER.error("Не найдено книг по жанру {}", genreName);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не найдено книг по заданному жанру");
        }
    }

    @GetMapping("/authors/{authorIds}")
    public List<BookDto> getBooksByAuthors(@PathVariable("authorIds") List<Long> authorIds) {
        LOGGER.info("Список книг по авторам ID {}", authorIds);
        try {
            List<BookDto> list = service.getBookListByAuthors(authorIds);
            sortBooks(list);
            return list;
        } catch (ApplDbNoDataFoundException e) {
            LOGGER.error("Не найдено книг по авторам ID {}", authorIds);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не найдено книг по заданным авторам");
        }
    }

    private void sortBooks(List<BookDto> list) {
        list.sort(Comparator
                .comparing(BookDto::getGenreName)
                .thenComparing(BookDto::getFirstAuthor)
                .thenComparing(BookDto::getTitle));
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable("id") Long id) {
        try {
            return BookDto.of(service.getBookById(id));
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга ID " + id + " не найдена");
        }
    }

    @PostMapping
    public void addBook(@RequestBody BookInput book) {
        if (book.getBookId() == null) {
            LOGGER.info("Новая книга {}", book.getTitle());
            service.addBook(book);
        } else {
            LOGGER.error("Добавление книги с имеющимся ID {}", book.getBookId());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "Невозможно добавить книгу с имеющимся ID");
        }
    }

    @PutMapping
    public void updateBook(@RequestBody BookInput book) {
        try {
            LOGGER.info("Корректировка книги ID {}", book.getBookId());
            service.updateBook(book);
        } catch (ApplDbNoDataFoundException e) {
            LOGGER.error("Ошибка корректировки книги. ID {} не найден", book.getBookId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга ID " + book.getBookId() + " не найдена");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        try {
            LOGGER.info("Удаление книги ID {}", id);
            service.deleteBook(id);
        } catch (ApplDbNoDataFoundException e) {
            LOGGER.error("Ошибка удаления книги. ID {} не найден", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга ID " + id + " не найдена");
        }
    }

}
