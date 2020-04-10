package ru.otus.job06.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Input;
import org.springframework.shell.Shell;
import ru.otus.job06.model.Book;
import ru.otus.job06.repository.BookRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Тестирование чтение списков книг.
 * Прохождения данных по цепочке бинов Shell - Controller - Repository и обратно.
 * Ограничить контекст не получается, т.к. для Spring Shell много чего необходимо.
 */
@DisplayName("Тест команд Spring Shell. Чтение списков книг")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShellBookRRRTest {
    public static final Input COMMAND_GET_LIST = () -> "book-get";
    public static final Input COMMAND_GET_LIST_SHORT = () -> "bg";
    public static final Input COMMAND_GET_LIST_SHORT_GENRE = () -> "bgg фантастика";
    public static final Input COMMAND_GET_LIST_SHORT_AUTHOR = () -> "bga стругацкий";

    @Autowired
    private TestShellUtil testShellUtil;

    @Autowired
    private Shell shell;

    @MockBean
    private BookRepository mockRepository;

    private List<Book> bookList;

    @BeforeEach
    private void init() {
        bookList = testShellUtil.createBookList();
    }

    @Test
    @Order(1)
    @DisplayName("Чтение полного списка")
    public void getListOkTest() {
        // Repository возвращает список.
        when(mockRepository.getBookList()).thenReturn(bookList);
        // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_GET_LIST)).contains("1").contains("2").contains("\n")
                .doesNotContain("3").contains("Понедельник начинается в субботу").contains("Азазель")
                .contains("Аркадий Стругацкий, Борис Стругацкий").contains("Борис Акунин");
    }

    @Test
    @Order(2)
    @DisplayName("Чтение списка по жанру")
    public void getListGenreOkTest() {
        // Repository возвращает список.
        // Здесь же проверяется, что параметр дошел до Repository.
        when(mockRepository.getBookListByGenre("фантастика")).thenReturn(bookList);
        // Команда (короткая) по жанру
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT_GENRE)).contains("1").contains("2").contains("\n")
                .doesNotContain("3").contains("Понедельник начинается в субботу").contains("Азазель")
                .contains("Аркадий Стругацкий, Борис Стругацкий").contains("Борис Акунин");
    }

    @Test
    @Order(3)
    @DisplayName("Чтение списка по автору")
    public void getListAuthorOkTest() {
        // Repository возвращает список.
        // Здесь же проверяется, что параметр дошел до Repository.
        when(mockRepository.getBookListByAuthor("стругацкий")).thenReturn(bookList);
        // Команда (короткая) по автору
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT_AUTHOR)).contains("1").contains("2").contains("\n")
                .doesNotContain("3").contains("Понедельник начинается в субботу").contains("Азазель")
                .contains("Аркадий Стругацкий, Борис Стругацкий").contains("Борис Акунин");
    }

    @Test
    @Order(10)
    @DisplayName("Чтение полного списка - Error")
    public void getListErrorTest() {
        // Repository возвращает пустой список.
        when(mockRepository.getBookList()).thenReturn(Collections.emptyList());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
    }

    @Test
    @Order(20)
    @DisplayName("Чтение полного списка - Exception")
    public void getListExceptionTest() {
        // Repository возвращает пустой список.
        when(mockRepository.getBookList()).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("DB error");
    }

}
