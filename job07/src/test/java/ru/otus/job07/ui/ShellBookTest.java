package ru.otus.job07.ui;

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
import ru.otus.job07.model.Author;
import ru.otus.job07.model.Book;
import ru.otus.job07.model.Genre;
import ru.otus.job07.repository.AuthorRepository;
import ru.otus.job07.repository.BookRepository;
import ru.otus.job07.repository.GenreRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование управления книгами: create, update, delete.
 * Прохождения данных по цепочке бинов Shell - Controller - Service - Repository и обратно.
 * Ограничить контекст не получается, т.к. для Spring Shell много чего необходимо.
 */
@DisplayName("Тест команд Spring Shell. Управление книгами")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShellBookTest {
    public static final Input COMMAND_GET_LIST = () -> "book-get";
    public static final Input COMMAND_GET_LIST_SHORT = () -> "bg";
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
        when(mockRepositoryAuthor.getByFirstNameIgnoreCaseAndLastNameIgnoreCase(any(), any()))
                .thenReturn(new Author(1L, "", ""))
                .thenReturn(new Author(2L, "", ""));
        when(mockRepositoryGenre.findByGenreNameIgnoreCase(any())).thenReturn(new Genre(1L, ""));
        when(mockRepository.save(any())).thenReturn(new Book(10L, "", null, null));
       // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_ADD)).isEqualTo("Новый ID: 10");
        // До Repository дошло заданное значение.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockRepository).save(captor.capture());
        Book expectedBook = bookList.get(0);
        expectedBook.setBookId(null);
        assertEquals(expectedBook, captor.getValue());
    }

    @Test
    @Order(31)
    @DisplayName("Create - Exception")
    public void addExceptionTest() {
        // Repository выбрасывает Exception.
        when(mockRepositoryAuthor.getByFirstNameIgnoreCaseAndLastNameIgnoreCase(any(), any()))
                .thenReturn(new Author(2L, "", ""));
        when(mockRepositoryGenre.findByGenreNameIgnoreCase(any())).thenReturn(new Genre(1L, ""));
        when(mockRepository.save(any())).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_ADD_SHORT)).startsWith("Ошибка").contains("DB error");
        // До Repository дошло заданное значение.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockRepository).save(captor.capture());
        Book expectedBook = bookList.get(1);
        expectedBook.setBookId(null);
        assertEquals(expectedBook.toString(), captor.getValue().toString());
    }

    @Test
    @Order(2)
    @DisplayName("Чтение полного списка")
    public void getListOkTest() {
        // Repository возвращает список.
        when(mockRepository.findAll()).thenReturn(bookList);
        // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_GET_LIST)).contains("1").contains("2").contains("\n")
                .doesNotContain("3").contains("Понедельник начинается в субботу").contains("Азазель")
                .contains("Аркадий Стругацкий, Борис Стругацкий").contains("Борис Акунин");
    }

    @Test
    @Order(22)
    @DisplayName("Чтение полного списка - Error")
    public void getListErrorTest() {
        // Repository возвращает пустой список.
        when(mockRepository.findAll()).thenReturn(Collections.emptyList());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
    }

    @Test
    @Order(32)
    @DisplayName("Чтение полного списка - Exception")
    public void getListExceptionTest() {
        // Repository возвращает пустой список.
        when(mockRepository.findAll()).thenThrow(new RuntimeException("DB error"));
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_GET_LIST_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(3)
    @DisplayName("Update - OK")
    public void updateOkTest() {
        Book book = bookList.get(0);
        book.setBookId(22L);
        // Repository возвращает непустую книгу, авторов и жанр.
        when(mockRepositoryAuthor.getByFirstNameIgnoreCaseAndLastNameIgnoreCase(any(), any()))
                .thenReturn(new Author(1L, "", ""))
                .thenReturn(new Author(2L, "", ""));
        when(mockRepositoryGenre.findByGenreNameIgnoreCase(any())).thenReturn(new Genre(1L, ""));
        when(mockRepository.existsById(anyLong())).thenReturn(true);
        // Команда (длинная) возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_UPDATE));
        // До Repository дошли значения из команды.
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(mockRepository).save(captor.capture());
        assertEquals(book, captor.getValue());
    }

    @Test
    @Order(23)
    @DisplayName("Update - Error")
    public void updateErrorTest() {
        // Repository возвращает false
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // Метод Repository не выполнялся.
        verify(mockRepository, never()).save(any());
    }

    @Test
    @Order(33)
    @DisplayName("Update - Exception")
    public void updateExceptionTest() {
        // Repository возвращает книгу.
        when(mockRepositoryAuthor.getByFirstNameIgnoreCaseAndLastNameIgnoreCase(any(), any()))
                .thenReturn(new Author(2L, "", ""));
        when(mockRepositoryGenre.findByGenreNameIgnoreCase(any())).thenReturn(new Genre(1L, ""));
        when(mockRepository.existsById(anyLong())).thenReturn(true);
        // и выбрасывает Exception.
        doThrow(new RuntimeException("DB error")).when(mockRepository).save(any());
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_UPDATE_SHORT)).startsWith("Ошибка").contains("DB error");
    }

    @Test
    @Order(4)
    @DisplayName("Delete - OK")
    public void deleteOkTest() {
        // Repository возвращает книгу.
        when(mockRepository.existsById(anyLong())).thenReturn(true);
        // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_DELETE));
        // До Repository дошло значение 100.
        verify(mockRepository).deleteById(100L);
    }

    @Test
    @Order(24)
    @DisplayName("Delete - Error")
    public void deleteErrorTest() {
        // Repository возвращает объект не найден.
        when(mockRepository.existsById(anyLong())).thenReturn(false);
        // Команда (краткая) возвращает пользователю сообщение об ошибке.
        assertThat((String) shell.evaluate(COMMAND_DELETE_SHORT)).startsWith("Ошибка").contains("Данные не найдены");
        // Метод Repository не выполнялся.
        verify(mockRepository, never()).delete(any());
    }

}
