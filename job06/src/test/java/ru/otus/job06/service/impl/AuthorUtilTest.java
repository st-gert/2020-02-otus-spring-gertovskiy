package ru.otus.job06.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.otus.job06.model.Author;
import ru.otus.job06.ui.TestShellUtil;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тест преобразований строк в объкт Автор и список авторов")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorUtilTest {

    private AuthorUtil authorUtil = new AuthorUtil();
    private TestShellUtil testShellUtil = new TestShellUtil();

    @Test
    @Order(0)
    @DisplayName("один автор")
    public void createAuthorTest() {
        Author testGonchrov = authorUtil.createAuthor("Иван           Гончаров");
        Author testPushkin = authorUtil.createAuthor("Александр Сергеевич Пушкин");
        Author correctGonchrov = new Author(null, "Иван", "Гончаров");
        Author correctPushkin = new Author(null, "Александр Сергеевич", "Пушкин");
        assertAll(
                () -> assertEquals(correctGonchrov, testGonchrov),
                () -> assertEquals(correctPushkin, testPushkin)
        );
    }

    @Test
    @Order(1)
    @DisplayName("спсок авторов")
    public void createAuthorListTest() {
        List<Author> authorList = testShellUtil.createAuthorList(); // список авторов для тестирования
        authorList.forEach(x -> x.setAuthorId(null));               // обнуляем ID, т.к. в новом они будут null
        String authorsString = authorList                           // список писателей через ",":
                .stream()                                           // Аркадий Стругацкий, Борис Стругацкий, Борис Акунин
                .map(Author::getFullName)
                .collect(Collectors.joining(", "));
        List<Author> testAuthorList = authorUtil.createAuthorList(authorsString);
        Author[] array = new Author[testAuthorList.size()];         // необх. преобразовать в массив
        assertThat(testAuthorList).containsOnly(testAuthorList.toArray(array));     // проверка!!!
    }
}
