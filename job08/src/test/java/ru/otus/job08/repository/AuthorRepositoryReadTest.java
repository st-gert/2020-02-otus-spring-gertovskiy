package ru.otus.job08.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.job08.model.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тест Repository Управление авторами")
@DataMongoTest
@ComponentScan({"ru.otus.job08.changelog"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorRepositoryReadTest {

    @Autowired
    AuthorRepository repository;

    @Test
    @Order(2)
    @DisplayName("Получение автора по имени")
    void getAuthorByName() {
        Author foundAuthor = repository.getByFirstNameIgnoreCaseAndLastNameIgnoreCase("кир", "булычев");
        assertAll(
                () -> assertNotNull(foundAuthor)
                , () -> assertEquals("Кир", foundAuthor.getFirstName())
                , () -> assertEquals("Булычев", foundAuthor.getLastName())
        );
    }

    @Test
    @Order(22)
    @DisplayName("Получение автора по имени - Error")
    void getAuthorByNameError() {
        Author foundAuthor = repository.getByFirstNameIgnoreCaseAndLastNameIgnoreCase("Дарья", "Донцова");
        assertThat(foundAuthor).isNull();
    }

}
