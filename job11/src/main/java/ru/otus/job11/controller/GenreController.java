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
import ru.otus.job11.model.Genre;
import ru.otus.job11.model.dto.GenreDto;
import ru.otus.job11.repository.GenreRepository;

@RestController
@RequestMapping("/api/genre")
public class GenreController {

    private final GenreRepository repository;

    public GenreController(GenreRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Flux<GenreDto> getAllGenres() {
        return repository.findAllByOrderByGenreName()
                .map(GenreDto::of)
                ;
    }

    @GetMapping("/{id}")
    public Mono<GenreDto> getGenreById(@PathVariable("id") String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр ID " + id + " не найден")))
                .map(GenreDto::of)
                ;
    }

    @PostMapping
    public Mono<GenreDto> addGenre(@RequestBody Genre genre) {
        if (genre.getId() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Невозможно добавить жанр с имеющимся ID");
        }
        return repository.insert(genre)
                .map(GenreDto::of)
                ;
    }

    @PutMapping
    public Mono<GenreDto> updateGenre(@RequestBody Genre genre) {
        return Mono.just(genre)
                .flatMap(g -> repository.findById(genre.getId()))
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр ID " + genre.getId() + " не найден")))
                .flatMap(g -> {
                    g.setGenreName(genre.getGenreName());
                    return repository.save(g);
                })
                .map(GenreDto::of)
                ;
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteGenre(@PathVariable("id") String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр ID " + id + " не найден")))
                .filter(genre -> genre.getBooks().size() == 0)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                                "Жанр ID " + id + " удалить невозможно. Есть связанные с ним книги.")))
                .flatMap(repository::delete)
                ;
    }

}
