package ru.otus.job11.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.job11.model.Author;

@DisplayName("Тест Repository Управление авторами")
@DataMongoTest
@ComponentScan({"ru.otus.job11.changelog"})
class AuthorRepositoryTest {

    @Autowired
    AuthorRepository repository;

    @Test
    @DisplayName("Получение отсортированного списка авторов")
    public void getAllTest() {
        Flux<String> authorNames = repository.findAllByOrderByLastNameAscFirstNameAsc()
                .map(Author::getFullName);
        StepVerifier
                .create(authorNames)
                .expectNext("Борис Акунин")
                .expectNextCount(5)
                .expectNext("Борис Стругацкий")
                .expectComplete()
                .verify()
                ;
    }
}