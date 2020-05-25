package ru.otus.job11.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.job11.model.Genre;

@DisplayName("Тест Repository Управление лит.жанрами")
@DataMongoTest
@ComponentScan({"ru.otus.job11.changelog"})
class GenreRepositoryTest {

    @Autowired
    GenreRepository repository;

    @Test
    @DisplayName("Получение отсортированного списка жанров")
    public void getAllTest() {
        Flux<String> genreNames = repository.findAllByOrderByGenreName()
                .map(Genre::getGenreName);
        StepVerifier
                .create(genreNames)
                .expectNext("Детектив")
                .expectNextCount(2)
                .expectNext("Фантастика")
                .expectComplete()
                .verify()
                ;
    }
}