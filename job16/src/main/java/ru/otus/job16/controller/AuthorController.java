package ru.otus.job16.controller;

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
import ru.otus.job16.exception.ApplDbConstraintException;
import ru.otus.job16.exception.ApplDbNoDataFoundException;
import ru.otus.job16.model.Author;
import ru.otus.job16.model.dto.AuthorDto;
import ru.otus.job16.service.AuthorService;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/author")
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        List<AuthorDto> list = service.getAuthorList();
        list.sort(Comparator
                .comparing(AuthorDto::getLastName)
                .thenComparing(AuthorDto::getFirstName)
        );
        return list;
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable("id") Long id) {
        try {
            return service.getAuthorById(id);
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор ID " + id + " не найден");
        }
    }

    @PostMapping
    public void addAuthor(@RequestBody Author author) {
        if (author.getAuthorId() == null) {
            service.addAuthor(author);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "Невозможно добавить автора с имеющимся ID");
        }
    }

    @PutMapping
    public void updateAuthor(@RequestBody Author author) {
        try {
            service.updateAuthor(author);
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор ID " + author.getAuthorId() + " не найден");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable("id") Long id) {
        try {
            service.deleteAuthor(id);
        } catch (ApplDbConstraintException e) {
            AuthorDto author = service.getAuthorById(id);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "Невозможно удалить автора " + author.getFirstName() + " " + author.getLastName() 
                            + ". Есть связанные с ним книги.");
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор ID " + id + " не найден");
        }
    }

}
