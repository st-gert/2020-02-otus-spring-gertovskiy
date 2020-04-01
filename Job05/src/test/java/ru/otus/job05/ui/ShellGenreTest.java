package ru.otus.job05.ui;

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
import ru.otus.job05.dao.GenreDao;
import ru.otus.job05.model.Genre;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование управления лит. жанрами.
 * Прохождения данных по цепочке бинов Shell - Controller - DAO и обратно.
 * Ограничить контекст не получается, т.к. для Spring Shell много чего необходимо.
 */
@DisplayName("Тест команд Spring Shell. Управление лит. жанрами")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShellGenreTest {
    public static final Input COMMAND_ADD = () -> "genre-add Детектив";
    public static final Input COMMAND_ADD_SHORT = () -> "ga МурА";
    public static final Input COMMAND_GET_LIST = () -> "genre-get";
    public static final Input COMMAND_GET_LIST_SHORT = () -> "gg";
    public static final Input COMMAND_UPDATE = () -> "genre-update 2 Детектив";
    public static final Input COMMAND_UPDATE_SHORT = () -> "gu 10 МурА";
    public static final Input COMMAND_DELETE = () -> "genre-delete 100";
    public static final Input COMMAND_DELETE_SHORT = () -> "gd 50";

    @Autowired
    private TestShellUtil testShellUtil;

    @Autowired
    private Shell shell;

    @MockBean
    private GenreDao mockDao;

    @Test
    @Order(1)
    @DisplayName("Create - OK")
    public void addOkTest() {
        // DAO возвращает ID.
        when(mockDao.addGenre(any())).thenReturn(10L);
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_ADD)).isEqualTo("Новый ID: 10");
        // До DAO дошло заданное значение.
        verify(mockDao).addGenre(new Genre(null, "Детектив"));
    }

    @Test
    @Order(31)
    @DisplayName("Create - Exception")
    public void addExceptionTest() {
        // DAO выбрасывает Exception.
        when(mockDao.addGenre(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_ADD_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(2)
    @DisplayName("Read list - OK")
    public void getListOkTest() {
        // DAO возвращает список.
        when(mockDao.getGenreList()).thenReturn(testShellUtil.createGenreList());
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_GET_LIST)).contains("1").contains("2").contains("\n")
                .contains("Фантастика").contains("Детектив");
    }

    @Test
    @Order(22)
    @DisplayName("Read list - Error")
    public void getListErrorTest() {
        // DAO возвращает пустой список.
        when(mockDao.getGenreList()).thenReturn(Collections.emptyList());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
    }

    @Test
    @Order(32)
    @DisplayName("Read list - Exception")
    public void getListExceptionTest() {
        // DAO выбрасывает Exception.
        when(mockDao.getGenreList()).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(3)
    @DisplayName("Update - OK")
    public void updateOkTest() {
        // DAO возвращает кол-во записей 1.
        when(mockDao.updateGenre(any())).thenReturn(1);
       // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_UPDATE));
        // До DAO дошли значения из команды.
        verify(mockDao).updateGenre(new Genre(2L, "Детектив"));
    }

    @Test
    @Order(23)
    @DisplayName("Update - Error")
    public void updateErrorTest() {
        // DAO возвращает кол-во записей 0.
        when(mockDao.updateGenre(any())).thenReturn(0);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // До DAO дошли значения из команды.
        verify(mockDao).updateGenre(new Genre(10L, "МурА"));
    }

    @Test
    @Order(33)
    @DisplayName("Update - Exception")
    public void updateExceptionTest() {
        // DAO выбрасывает Exception.
        when(mockDao.updateGenre(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(4)
    @DisplayName("Delete - OK")
    public void deleteOkTest() {
        // DAO возвращает кол-во записей 1.
        when(mockDao.deleteGenre(anyLong())).thenReturn(1);
       // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_DELETE));
        // До DAO дошло значение 100.
        verify(mockDao).deleteGenre(100L);
    }

    @Test
    @Order(24)
    @DisplayName("Delete - Error")
    public void deleteErrorTest() {
        // DAO возвращает кол-во записей 0.
        when(mockDao.deleteGenre(anyLong())).thenReturn(0);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // До DAO дошло значение 50.
        verify(mockDao).deleteGenre(50L);
    }

    @Test
    @Order(25)
    @DisplayName("Delete - Error 2")
    public void deleteError2Test() {
        // DAO возвращает признак нарушения constraint.
        when(mockDao.deleteGenre(anyLong())).thenReturn(-1);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("Операция запрещена");
        // До DAO дошло значение 50.
        verify(mockDao).deleteGenre(50L);
    }

    @Test
    @Order(34)
    @DisplayName("Delete - Exception")
    public void deleteExceptionTest() {
        // DAO выбрасывает Exception.
        when(mockDao.deleteGenre(anyLong())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("DB error");
    }

}
