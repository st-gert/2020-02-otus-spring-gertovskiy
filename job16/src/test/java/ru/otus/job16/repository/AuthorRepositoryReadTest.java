package ru.otus.job16.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.job16.model.Author;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Repository Управление авторами")
@DataJpaTest
public class AuthorRepositoryReadTest {

    @Autowired
    AuthorRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Получение списка авторов по списку ID")
    @Test
    void getAuthorListByIds() {
        List<Author> authors = repository.findByAuthorIdIn(Arrays.asList(2L, 6L));
        assertThat(authors)
                .hasSize(2)
                .matches(Objects::nonNull)
                .extracting(Author::getFullName)
                .containsOnly("Борис Стругацкий", "Борис Акунин")
        ;
    }
}
