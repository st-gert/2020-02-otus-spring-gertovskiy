package ru.otus.job05.ui;

import org.apache.commons.lang3.tuple.Pair;
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
import ru.otus.job05.dao.BookDao;
import ru.otus.job05.model.Book;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Тестирование чтение списков книг.
 * Прохождения данных по цепочке бинов Shell - Controller - DAO и обратно.
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
    private BookDao mockDao;

    private List<Book> bookList;

    @BeforeEach
    private void init() {
        bookList = testShellUtil.createBookList();
    }

    @Test
    @Order(1)
    @DisplayName("Чтение полного списка")
    public void getListOkTest() {
        // DAO возвращает список.
        when(mockDao.getBookList()).thenReturn(bookList);
        // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_GET_LIST)).contains("1").contains("2").contains("\n")
                .doesNotContain("3").contains("Понедельник начинается в субботу").contains("Азазель")
                .contains("Аркадий Стругацкий, Борис Стругацкий").contains("Борис Акунин");
    }

    @Test
    @Order(2)
    @DisplayName("Чтение списка по жанру")
    public void getListGenreOkTest() {
        // DAO возвращает список.
        // Здесь же проверяется, что параметр дошел до DAO.
        when(mockDao.getBookListByGenre("фантастика")).thenReturn(bookList);
        // Команда (короткая) по жанру
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT_GENRE)).contains("1").contains("2").contains("\n")
                .doesNotContain("3").contains("Понедельник начинается в субботу").contains("Азазель")
                .contains("Аркадий Стругацкий, Борис Стругацкий").contains("Борис Акунин");
    }

    @Test
    @Order(3)
    @DisplayName("Чтение списка по автору")
    public void getListAuthorOkTest() {
        // DAO возвращает список.
        // Здесь же проверяется, что параметр дошел до DAO.
        when(mockDao.getBookListByAuthor("стругацкий")).thenReturn(bookList);
        // Команда (короткая) по автору
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT_AUTHOR)).contains("1").contains("2").contains("\n")
                .doesNotContain("3").contains("Понедельник начинается в субботу").contains("Азазель")
                .contains("Аркадий Стругацкий, Борис Стругацкий").contains("Борис Акунин");
    }

    @Test
    @Order(10)
    @DisplayName("Чтение полного списка - Error")
    public void getListErrorTest() {
        // DAO возвращает пустой список.
        when(mockDao.getBookList()).thenReturn(Collections.emptyList());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
    }

    @Test
    @Order(20)
    @DisplayName("Чтение полного списка - Exception")
    public void getListExceptionTest() {
        // DAO возвращает пустой список.
        when(mockDao.getBookList()).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("DB error");
    }

}
