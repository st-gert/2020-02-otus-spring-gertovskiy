package ru.otus.job06.ui;

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
import ru.otus.job06.repository.AuthorRepository;
import ru.otus.job06.repository.BookRepository;
import ru.otus.job06.repository.GenreRepository;
import ru.otus.job06.model.Author;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование управления книгами: create, update, delete.
 * Прохождения данных по цепочке бинов Shell - Controller - Repository и обратно.
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
    private BookRepository mockRepository;
    @MockBean
    private AuthorRepository mockRepositoryAuthor;
    @MockBean
    private GenreRepository mockRepositoryGenre;

    private List<Book> bookList;

    @BeforeEach
    private void init() {
        bookList = testShellUtil.createBookList();
    }

    @Test
    @Order(1)
    @DisplayName("Create - OK")
    public void addOkTest() {
        // Repository возвращает ID.
        when(mockRepositoryAuthor.getAuthorByName(any()))
                .thenReturn(Optional.of(new Author(1L, "", "")))
                .thenReturn(Optional.of(new Author(2L, "", "")));
        when(mockRepositoryGenre.getGenreByName(any())).thenReturn(Optional.of(new Genre(1L, "")));
        when(mockRepository.addBook(any())).thenReturn(10L);
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_ADD)).isEqualTo("Новый ID: 10");
        // До Repository дошло заданное значение.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockRepository).addBook(captor.capture());
        Book expectedBook = bookList.get(0);
        expectedBook.setBookId(null);
        assertEquals(expectedBook, captor.getValue());
    }

    @Test
    @Order(31)
    @DisplayName("Create - Exception")
    public void addExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepository.addBook(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_ADD_SHORT)).startsWith("Ошибка").contains("DB error");
        // До Repository дошло заданное значение.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockRepository).addBook(captor.capture());
        Book expectedBook = bookList.get(1);
        expectedBook.setBookId(null);
        assertEquals(expectedBook.toString(), captor.getValue().toString());
    }

    @Test
    @Order(3)
    @DisplayName("Update - OK")
    public void updateOkTest() {
        Book book = bookList.get(0);
        book.setBookId(22L);
        // Repository возвращает новый объект.
        when(mockRepositoryAuthor.getAuthorByName(any()))
                .thenReturn(Optional.of(new Author(1L, "", "")))
                .thenReturn(Optional.of(new Author(2L, "", "")));
        when(mockRepositoryGenre.getGenreByName(any())).thenReturn(Optional.of(new Genre(1L, "")));
        when(mockRepository.updateBook(any())).thenReturn(Optional.of(book));
        // Команда (длинная) возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_UPDATE));
        // До Repository дошли значения из команды.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockRepository).updateBook(captor.capture());
        assertEquals(book, captor.getValue());
    }

    @Test
    @Order(23)
    @DisplayName("Update - Error")
    public void updateErrorTest() {
        Book book = bookList.get(1);
        book.setBookId(10L);
        // Repository возвращает сообщение об ошибке.
        when(mockRepository.updateBook(any())).thenReturn(Optional.empty());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // До Repository дошло заданное значение.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockRepository).updateBook(captor.capture());
        assertEquals(book.toString(), captor.getValue().toString());
    }

    @Test
    @Order(33)
    @DisplayName("Update - Exception")
    public void updateExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepository.updateBook(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(4)
    @DisplayName("Delete - OK")
    public void deleteOkTest() {
        // Repository возвращает сообщение true.
        when(mockRepository.deleteBook(any())).thenReturn(1);
        // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_DELETE));
        // До Repository дошло значение 100.
        verify(mockRepository).deleteBook(100L);
    }

    @Test
    @Order(24)
    @DisplayName("Delete - Error")
    public void deleteErrorTest() {
        // Repository возвращает сообщение об ошибке.
        when(mockRepository.deleteBook(anyLong())).thenReturn(0);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // До Repository дошло значение 50.
        verify(mockRepository).deleteBook(50L);
    }

}
