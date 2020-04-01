package ru.otus.job05.ui;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Input;
import org.springframework.shell.Shell;
import ru.otus.job05.dao.AuthorDao;
import ru.otus.job05.model.Author;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование управления авторами.
 * Прохождения данных по цепочке бинов Shell - Controller - DAO и обратно.
 * Ограничить контекст не получается, т.к. для Spring Shell много чего необходимо.
 */
@DisplayName("Тест команд Spring Shell. Управление авторами")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShellAuthorTest {
    public static final Input COMMAND_ADD = FixedInput.of("author-add", "Иван Хлестаков");
    public static final Input COMMAND_ADD_SHORT = FixedInput.of("aa", "Имя Фамилия");
    public static final Input COMMAND_GET_LIST = () -> "author-get";
    public static final Input COMMAND_GET_LIST_SHORT = () -> "ag";
    public static final Input COMMAND_UPDATE = FixedInput.of("author-update", "22", "Павел Чичиков");
    public static final Input COMMAND_UPDATE_SHORT = FixedInput.of("au", "10", "Имя Фамилия");
    public static final Input COMMAND_DELETE = () -> "author-delete 100";
    public static final Input COMMAND_DELETE_SHORT = () -> "ad 50";

    @Autowired
    private TestShellUtil testShellUtil;

    @Autowired
    private Shell shell;

    @MockBean
    private AuthorDao mockDao;

    @Test
    @Order(1)
    @DisplayName("Create - OK")
    public void addOkTest() {
        // DAO возвращает ID.
        when(mockDao.addAuthor(any())).thenReturn(10L);
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_ADD)).isEqualTo("Новый ID: 10");
        // До DAO дошло заданное значение.
        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(mockDao).addAuthor(captor.capture());
        assertEquals(new Author(null,"Иван", "Хлестаков"), captor.getValue());
    }

    @Test
    @Order(31)
    @DisplayName("Create - Exception")
    public void addExceptionTest() {
        // DAO выбрасывает Exception.
        when(mockDao.addAuthor(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_ADD_SHORT)).startsWith("Ошибка").contains("DB error");
        // До DAO дошло заданное значение.
        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(mockDao).addAuthor(captor.capture());
        assertEquals(new Author(null,"Имя", "Фамилия"), captor.getValue());
    }

    @Test
    @Order(2)
    @DisplayName("Read list - OK")
    public void getListOkTest() {
        // DAO возвращает список.
        when(mockDao.getAuthorList()).thenReturn(testShellUtil.createAuthorList());
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_GET_LIST)).contains("1").contains("2").contains("3").contains("\n")
                .contains("Аркадий").contains("Борис").contains("Стругацкий").contains("Акунин");
    }

    @Test
    @Order(22)
    @DisplayName("Read list - Error")
    public void getListErrorTest() {
        // DAO возвращает пустой список.
        when(mockDao.getAuthorList()).thenReturn(Collections.emptyList());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
    }

    @Test
    @Order(32)
    @DisplayName("Read list - Exception")
    public void getListExceptionTest() {
        // DAO выбрасывает Exception.
        when(mockDao.getAuthorList()).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(3)
    @DisplayName("Update - OK")
    public void updateOkTest() {
        // DAO возвращает кол-во записей 1.
        when(mockDao.updateAuthor(any())).thenReturn(1);
       // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_UPDATE));
        // До DAO дошли значения из команды.
        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(mockDao).updateAuthor(captor.capture());
        assertEquals(new Author(22L,"Павел", "Чичиков"), captor.getValue());
    }

    @Test
    @Order(23)
    @DisplayName("Update - Error")
    public void updateErrorTest() {
        // DAO возвращает кол-во записей 0.
        when(mockDao.updateAuthor(any())).thenReturn(0);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // До DAO дошло заданное значение.
        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(mockDao).updateAuthor(captor.capture());
        assertEquals(new Author(10L,"Имя", "Фамилия"), captor.getValue());
    }

    @Test
    @Order(33)
    @DisplayName("Update - Exception")
    public void updateExceptionTest() {
        // DAO выбрасывает Exception.
        when(mockDao.updateAuthor(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(4)
    @DisplayName("Delete - OK")
    public void deleteOkTest() {
        // DAO возвращает кол-во записей 1.
        when(mockDao.deleteAuthor(anyLong())).thenReturn(1);
       // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_DELETE));
        // До DAO дошло значение 100.
        verify(mockDao).deleteAuthor(100L);
    }

    @Test
    @Order(24)
    @DisplayName("Delete - Error")
    public void deleteErrorTest() {
        // DAO возвращает кол-во записей 0.
        when(mockDao.deleteAuthor(anyLong())).thenReturn(0);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        verify(mockDao).deleteAuthor(50L);
    }


    @Test
    @Order(34)
    @DisplayName("Delete - Exception")
    public void deleteExceptionTest() {
        // DAO выбрасывает Exception.
        when(mockDao.deleteAuthor(anyLong())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("DB error");
    }
}
