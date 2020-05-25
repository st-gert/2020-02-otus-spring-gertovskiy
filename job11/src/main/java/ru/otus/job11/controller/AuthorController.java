package ru.otus.job11.controller;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.otus.job11.model.Author;
import ru.otus.job11.model.dto.AuthorDto;
import ru.otus.job11.repository.AuthorRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class AuthorController {

    private final AuthorRepository repository;

    public AuthorController(AuthorRepository repository) {
        this.repository = repository;
    }

    @Bean
    public RouterFunction<ServerResponse> authorRoutes() {
        return route()
                .GET("/api/author", this::findAll)
                .GET("/api/author/{id}", this::findById)
                .POST("/api/author", this::insert)
                .PUT("/api/author", this::save)
                .DELETE("/api/author/{id}", this::delete)
                .build();
    }

    private Mono<ServerResponse> findAll(ServerRequest request) {
        return repository.findAllByOrderByLastNameAscFirstNameAsc()
                .map(AuthorDto::of)
                .collectList()
                .flatMap(list -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(list)
                );
    }

    private Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор ID " + id + " не найден")
                ))
                .map(AuthorDto::of)
                .flatMap(author -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(author)
                );
    }

    private Mono<ServerResponse> insert(ServerRequest request) {
        return request
                .bodyToMono(Author.class)
                .filter(author -> author.getId() == null)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                                "Невозможно добавить жанр с имеющимся ID")))
                .flatMap(repository::insert)
                .map(AuthorDto::of)
                .flatMap(author -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(author)
                )
                ;
    }

    private Mono<ServerResponse> save(ServerRequest request) {
        return request
                .bodyToMono(Author.class)
                .map(author -> Pair.of(author, repository.findById(author.getId())))
                .filter(pair -> !pair.getRight().equals(Mono.empty()))
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор не найден")))
                .flatMap(pair -> pair.getRight().flatMap(author -> {
                            author.setFirstName(pair.getLeft().getFirstName());
                            author.setLastName(pair.getLeft().getLastName());
                            return repository.save(author);
                        })
                )
                .map(AuthorDto::of)
                .flatMap(author -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(author)
                )
                ;
    }

    private Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор ID " + id + " не найден")))
                .filter(author -> author.getBooks().size() == 0)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                                "Удалить автора невозможно. Есть связанные с ним книги.")))
                .flatMap(repository::delete)
                .flatMap(v -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue("")
                )
                ;
    }

}