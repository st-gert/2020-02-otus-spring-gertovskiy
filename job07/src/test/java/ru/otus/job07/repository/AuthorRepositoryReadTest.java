package ru.otus.job07.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.job07.model.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тест Repository Управление авторами")
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorRepositoryReadTest {

    @Autowired
    AuthorRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @Order(2)
    @DisplayName("Получение автора по имени")
    void getAuthorByName() {
        Author foundAuthor = repository.getByFirstNameIgnoreCaseAndLastNameIgnoreCase("кир", "булычев");
        assertAll(
                () -> assertNotNull(foundAuthor)
                , () -> assertEquals(5L, foundAuthor.getAuthorId())
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
