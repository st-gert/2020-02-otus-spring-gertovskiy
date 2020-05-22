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
import ru.otus.job10.exception.ApplDbConstraintException;
import ru.otus.job10.exception.ApplDbNoDataFoundException;
import ru.otus.job10.model.Genre;
import ru.otus.job10.model.dto.GenreDto;
import ru.otus.job10.service.GenreService;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/genre")
public class GenreController {

    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public List<GenreDto> getAllGenres() {
        List<GenreDto> list = service.getGenreList();
        list.sort(Comparator.comparing(GenreDto::getGenreName));
        return list;
    }

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable("id") Long id) {
        try {
            return service.getGenreById(id);
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр ID " + id + " не найден");
        }
    }

    @PostMapping
    public void addGenre(@RequestBody Genre genre) {
        if (genre.getGenreId() == null) {
            service.addGenre(genre);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "Невозможно добавить жанр с имеющимся ID");
        }
    }

    @PutMapping
    public void updateGenre(@RequestBody Genre genre) {
        try {
            service.updateGenre(genre);
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр ID " + genre.getGenreId() + " не найден");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable("id") Long id) {
        try {
            service.deleteGenre(id);
        } catch (ApplDbConstraintException e) {
            GenreDto genre = service.getGenreById(id);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "Невозможно удалить жанр " + genre.getGenreName() + ". Есть связанные с ним книги.");
        } catch (ApplDbNoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр ID " + id + " не найден");
        }
    }

}
