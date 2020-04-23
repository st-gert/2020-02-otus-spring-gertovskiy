package ru.otus.job08.ui;

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
import ru.otus.job08.model.Book;
import ru.otus.job08.model.Genre;
import ru.otus.job08.repository.GenreRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование управления лит. жанрами.
 * Прохождения данных по цепочке бинов Shell - Controller - Service - Repository и обратно.
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
    private GenreRepository mockRepository;

    @Test
    @Order(1)
    @DisplayName("Create - OK")
    public void addOkTest() {
        // Repository возвращает ID.
        Genre genre = new Genre();
        genre.setId("10");
        when(mockRepository.save(any())).thenReturn(genre);
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_ADD)).isEqualTo("Новый ID: 10");
        // До Repository дошло заданное значение.
        verify(mockRepository).save(new Genre("Детектив"));
    }

    @Test
    @Order(31)
    @DisplayName("Create - Exception")
    public void addExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepository.save(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_ADD_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(2)
    @DisplayName("Read list - OK")
    public void getListOkTest() {
        // Repository возвращает список.
        when(mockRepository.findAll()).thenReturn(testShellUtil.createGenreList());
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_GET_LIST)).contains("1").contains("2").contains("\n")
                .contains("Фантастика").contains("Детектив");
    }

    @Test
    @Order(22)
    @DisplayName("Read list - Error")
    public void getListErrorTest() {
        // Repository возвращает пустой список.
        when(mockRepository.findAll()).thenReturn(Collections.emptyList());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
    }

    @Test
    @Order(32)
    @DisplayName("Read list - Exception")
    public void getListExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepository.findAll()).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(3)
    @DisplayName("Update - OK")
    public void updateOkTest() {
        // Repository возвращает существующий объект.
        Genre genre = new Genre("Детектив");
        genre.setId("2");
        when(mockRepository.findById(any())).thenReturn(Optional.of(genre));
       // Команда (длинная) возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_UPDATE));
        // До Repository дошли значения из команды.
        verify(mockRepository).save(genre);
    }

    @Test
    @Order(23)
    @DisplayName("Update - Error")
    public void updateErrorTest() {
        // Repository возвращает NULL - объект не найден.
        when(mockRepository.findById(any())).thenReturn(Optional.empty());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // Метод Repository не выполнялся.
        verify(mockRepository, never()).save(any());
    }

    @Test
    @Order(33)
    @DisplayName("Update - Exception")
    public void updateExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepository.findById(any())).thenReturn(Optional.of(new Genre()));
        doThrow(new RuntimeException("DB error")).when(mockRepository).save(any());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(4)
    @DisplayName("Delete - OK")
    public void deleteOkTest() {
        // Repository возвращает существующий объект.
        when(mockRepository.findById(any())).thenReturn(Optional.of(new Genre()));
       // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_DELETE));
        // До Repository дошло значение.
        verify(mockRepository).deleteById("100");
    }

    @Test
    @Order(24)
    @DisplayName("Delete - Error")
    public void deleteErrorTest() {
        // Repository возвращает Null - объект не найден.
        when(mockRepository.findById(any())).thenReturn(Optional.empty());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // Метод Repository не выполнялся.
        verify(mockRepository, never()).delete(any());
    }

    @Test
    @Order(25)
    @DisplayName("Delete - Error 2")
    public void deleteError2Test() {
        // Repository возвращает признак нарушения constraint.
        Genre genre = new Genre();
        genre.getBooks().add(new Book());
        when(mockRepository.findById(any())).thenReturn(Optional.of(genre));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("Операция запрещена");
        // До Repository дело не доходит.
        verify(mockRepository, never()).deleteById(any());
    }

    @Test
    @Order(34)
    @DisplayName("Delete - Exception")
    public void deleteExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepository.findById(any())).thenReturn(Optional.of(new Genre()));
        doThrow(new RuntimeException("DB error")).when(mockRepository).deleteById(any());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("DB error");
    }

}
