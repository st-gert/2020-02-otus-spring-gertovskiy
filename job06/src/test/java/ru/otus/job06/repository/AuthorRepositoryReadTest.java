package ru.otus.job06.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.job06.model.Author;
import ru.otus.job06.repository.impl.AuthorRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тест Repository Управление авторами")
@DataJpaTest
@Import({AuthorRepositoryImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorRepositoryReadTest {

    @Autowired
    AuthorRepositoryImpl repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @Order(2)
    @DisplayName("Получение автора по имени")
    void getAuthorByName() {
        Author author = new Author(null, "Кир", "Булычев");
        Author foundAuthor = repository.getAuthorByName(author);
        assertAll(
                () -> assertNotNull(foundAuthor)
                , () -> assertEquals(author.getFullName(), foundAuthor.getFullName())
        );
    }

    @Test
    @Order(22)
    @DisplayName("Получение автора по имени - Error")
    void getAuthorByNameError() {
        Author author = new Author(null, "Дарья", "Донцова");
        Author foundAuthor = repository.getAuthorByName(author);
        assertThat(foundAuthor).isNull();
    }

}
