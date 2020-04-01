package ru.otus.job05.ui;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
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
import ru.otus.job05.dao.BookDao;
import ru.otus.job05.model.Author;
import ru.otus.job05.model.Book;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование управления книгами: create, update, delete.
 * Прохождения данных по цепочке бинов Shell - Controller - DAO и обратно.
 * Ограничить контекст не получается, т.к. для Spring Shell много чего необходимо.
 */
@DisplayName("Тест команд Spring Shell. Управление книгами")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShellBookCUDTest {
    public static final Input COMMAND_ADD = FixedInput.of("book-add", "Понедельник начинается в субботу",
            "Фантастика", "Аркадий Стругацкий, Борис Стругацкий");
    public static final Input COMMAND_ADD_SHORT = FixedInput.of("ba", "Азазель", "Детектив", "Борис Акунин");
    public static final Input COMMAND_UPDATE = FixedInput.of("book-update", "22",
            "Понедельник начинается в субботу", "Фантастика", "Аркадий Стругацкий, Борис Стругацкий");
    public static final Input COMMAND_UPDATE_SHORT = FixedInput.of("bu", "10",
            "Азазель", "Детектив", "Борис Акунин");
    public static final Input COMMAND_DELETE = () -> "book-delete 100";
    public static final Input COMMAND_DELETE_SHORT = () -> "bd 50";

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
        bookList.forEach(x -> {
            x.setBookId(null);
            x.getGenre().setGenreId(null);
            x.getAuthors().forEach(a -> a.setAuthorId(null));
            }
        );
    }

    @Test
    @Order(1)
    @DisplayName("Create - OK")
    public void addOkTest() {
        // DAO возвращает ID.
        when(mockDao.addBook(any())).thenReturn(10L);
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_ADD)).isEqualTo("Новый ID: 10");
        // До DAO дошло заданное значение.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockDao).addBook(captor.capture());
        assertEquals(bookList.get(0), captor.getValue());
    }

    @Test
    @Order(31)
    @DisplayName("Create - Exception")
    public void addExceptionTest() {
        // DAO выбрасывает Exception.
        when(mockDao.addBook(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_ADD_SHORT)).startsWith("Ошибка").contains("DB error");
        // До DAO дошло заданное значение.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockDao).addBook(captor.capture());
        assertEquals(bookList.get(1), captor.getValue());
    }

    @Test
    @Order(3)
    @DisplayName("Update - OK")
    public void updateOkTest() {
        Book book = bookList.get(0);
        book.setBookId(22L);
        // DAO возвращает новый объект.
        when(mockDao.updateBook(any())).thenReturn(Optional.of(book));
        // Команда (длинная) возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_UPDATE));
        // До DAO дошли значения из команды.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockDao).updateBook(captor.capture());
        assertEquals(book, captor.getValue());
    }

    @Test
    @Order(23)
    @DisplayName("Update - Error")
    public void updateErrorTest() {
        Book book = bookList.get(1);
        book.setBookId(10L);
        // DAO возвращает сообщение об ошибке.
        when(mockDao.updateBook(any())).thenReturn(Optional.empty());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // До DAO дошло заданное значение.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockDao).updateBook(captor.capture());
        assertEquals(book, captor.getValue());
    }

    @Test
    @Order(33)
    @DisplayName("Update - Exception")
    public void updateExceptionTest() {
        // DAO выбрасывает Exception.
        when(mockDao.updateBook(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(4)
    @DisplayName("Delete - OK")
    public void deleteOkTest() {
        // DAO возвращает сообщение true.
        when(mockDao.deleteBook(any())).thenReturn(1);
        // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_DELETE));
        // До DAO дошло значение 100.
        verify(mockDao).deleteBook(100L);
    }

    @Test
    @Order(24)
    @DisplayName("Delete - Error")
    public void deleteErrorTest() {
        // DAO возвращает сообщение об ошибке.
        when(mockDao.deleteBook(anyLong())).thenReturn(0);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // До DAO дошло значение 50.
        verify(mockDao).deleteBook(50L);
    }

    @Test
    @Order(25)
    @DisplayName("Delete - Error2")
    public void deleteError2Test() {
        // DAO возвращает признак нарушения constraint.
        when(mockDao.deleteBook(anyLong())).thenReturn(-1);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("Операция запрещена");
        // До DAO дошло значение 50.
        verify(mockDao).deleteBook(50L);
    }

}
