package ru.otus.job06.ui;

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
import ru.otus.job06.model.Author;
import ru.otus.job06.repository.AuthorRepository;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование управления авторами.
 * Прохождения данных по цепочке бинов Shell - Controller - Service - Repository и обратно.
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
    private AuthorRepository mockRepository;

    @Test
    @Order(1)
    @DisplayName("Create - OK")
    public void addOkTest() {
        // Repository возвращает ID.
        when(mockRepository.addAuthor(any())).thenReturn(10L);
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_ADD)).isEqualTo("Новый ID: 10");
        // До Repository дошло заданное значение.
        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(mockRepository).addAuthor(captor.capture());
        assertEquals(new Author(null,"Иван", "Хлестаков"), captor.getValue());
    }

    @Test
    @Order(31)
    @DisplayName("Create - Exception")
    public void addExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepository.addAuthor(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_ADD_SHORT)).startsWith("Ошибка").contains("DB error");
        // До Repository дошло заданное значение.
        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(mockRepository).addAuthor(captor.capture());
        assertEquals(new Author(null,"Имя", "Фамилия"), captor.getValue());
    }

    @Test
    @Order(2)
    @DisplayName("Read list - OK")
    public void getListOkTest() {
        // Repository возвращает список.
        when(mockRepository.getAuthorList()).thenReturn(testShellUtil.createAuthorList());
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_GET_LIST)).contains("1").contains("2").contains("3").contains("\n")
                .contains("Аркадий").contains("Борис").contains("Стругацкий").contains("Акунин");
    }

    @Test
    @Order(22)
    @DisplayName("Read list - Error")
    public void getListErrorTest() {
        // Repository возвращает пустой список.
        when(mockRepository.getAuthorList()).thenReturn(Collections.emptyList());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
    }

    @Test
    @Order(32)
    @DisplayName("Read list - Exception")
    public void getListExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepository.getAuthorList()).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(3)
    @DisplayName("Update - OK")
    public void updateOkTest() {
        // Repository возвращает существующий объект.
        when(mockRepository.getAuthorById(anyLong())).thenReturn(new Author(22L, "Павел", "Чичиков"));
       // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_UPDATE));
        // До Repository дошли значения из команды.
        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(mockRepository).updateAuthor(captor.capture());
        assertEquals(new Author(22L,"Павел", "Чичиков"), captor.getValue());
    }

    @Test
    @Order(23)
    @DisplayName("Update - Error")
    public void updateErrorTest() {
        // Repository возвращает NULL - объект не найден.
        when(mockRepository.getAuthorById(anyLong())).thenReturn(null);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // Метод Repository не выполнялся.
        verify(mockRepository, never()).updateAuthor(any());
    }

    @Test
    @Order(33)
    @DisplayName("Update - Exception")
    public void updateExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepository.getAuthorById(anyLong())).thenReturn(new Author(2L, "", ""));
        doThrow(new RuntimeException("DB error")).when(mockRepository).updateAuthor(any());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(4)
    @DisplayName("Delete - OK")
    public void deleteOkTest() {
        Author author = new Author(100L, "Имя", "Фамилия");
        // Repository возвращает существующий объект.
        when(mockRepository.getAuthorById(anyLong())).thenReturn(author);
        // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_DELETE));
        // До Repository дошло значение 100.
        verify(mockRepository).deleteAuthor(author);
    }

    @Test
    @Order(24)
    @DisplayName("Delete - Error")
    public void deleteErrorTest() {
        // Repository возвращает Null - объект не найден.
        when(mockRepository.getAuthorById(anyLong())).thenReturn(null);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // Метод Repository не выполнялся.
        verify(mockRepository, never()).deleteAuthor(any());
    }

    @Test
    @Order(34)
    @DisplayName("Delete - Exception")
    public void deleteExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepository.getAuthorById(anyLong())).thenReturn(new Author(2L, "Имя", "Фамилия"));
        doThrow(new RuntimeException("DB error")).when(mockRepository).deleteAuthor(any());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("DB error");
    }
}
