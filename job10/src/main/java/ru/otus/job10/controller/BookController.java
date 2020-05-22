package ru.otus.job10.controller;

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
import ru.otus.job10.exception.ApplDbNoDataFoundException;
import ru.otus.job10.model.dto.BookDto;
import ru.otus.job10.model.dto.BookInput;
import ru.otus.job10.service.BookService;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookDto> getAllBooks() {
        List<BookDto> list = service.getBookList();
        sortBooks(list);
        return list;
    }

    @GetMapping("/genre/{genreId}")
    public List<BookDto> getBooksByGenre(@PathVariable("genreId") Long genreId) {
        try {
            List<BookDto> list = service.getBookListByGenre(genreId);
            sortBooks(list);
            return list;
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не найдено книг по заданному жанру");
        }
    }

    @GetMapping("/authors/{authorIds}")
    public List<BookDto> getBooksByAuthors(@PathVariable("authorIds") List<Long> authorIds) {
        try {
            List<BookDto> list = service.getBookListByAuthors(authorIds);
            sortBooks(list);
            return list;
        } catch (ApplDbNoDataFoundException e) {
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
            service.addBook(book);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "Невозможно добавить книгу с имеющимся ID");
        }
    }

    @PutMapping
    public void updateBook(@RequestBody BookInput book) {
        try {
            service.updateBook(book);
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга ID " + book.getBookId() + " не найдена");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        try {
            service.deleteBook(id);
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга ID " + id + " не найдена");
        }
    }

}
