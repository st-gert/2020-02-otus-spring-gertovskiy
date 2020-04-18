package ru.otus.job07.controller.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.otus.job07.model.Author;
import ru.otus.job07.ui.TestShellUtil;

import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Тест преобразований строк в объкт Автор и список авторов")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorUtilTest {

    private TestShellUtil testShellUtil = new TestShellUtil();

    @Test
    @Order(0)
    @DisplayName("один автор")
    public void createAuthorTest() {
        Author testGonchrov = AuthorUtil.createAuthor("Иван           Гончаров");
        Author testPushkin = AuthorUtil.createAuthor("Александр Сергеевич Пушкин");
        Author correctGonchrov = new Author(null, "Иван", "Гончаров");
        Author correctPushkin = new Author(null, "Александр Сергеевич", "Пушкин");
        Assertions.assertAll(
                () -> Assertions.assertEquals(correctGonchrov, testGonchrov),
                () -> Assertions.assertEquals(correctPushkin, testPushkin)
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
        List<Author> testAuthorList = AuthorUtil.createAuthorList(authorsString);
        Author[] array = new Author[testAuthorList.size()];         // необх. преобразовать в массив
        org.assertj.core.api.Assertions.assertThat(testAuthorList).containsOnly(testAuthorList.toArray(array));     // проверка!!!
    }
}
