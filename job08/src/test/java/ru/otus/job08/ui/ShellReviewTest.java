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
import ru.otus.job08.model.Review;
import ru.otus.job08.repository.BookRepository;
import ru.otus.job08.repository.ReviewRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование управления отзывами.
 * Прохождения данных по цепочке бинов Shell - Controller - Service - Repository и обратно.
 * Ограничить контекст не получается, т.к. для Spring Shell много чего необходимо.
 */
@DisplayName("Тест команд Spring Shell. Управление отзывами на книги")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShellReviewTest {
    public static final Input COMMAND_ADD = () -> "review-add 1 Отзыв";
    public static final Input COMMAND_UPDATE = () -> "review-update 1 Отзыв";
    public static final Input COMMAND_DELETE = () -> "review-delete 100";


    @Autowired
    private TestShellUtil testShellUtil;

    @Autowired
    private Shell shell;

    @MockBean
    private ReviewRepository mockRepository;
    @MockBean
    private BookRepository mockBookRepository;

    @Test
    @Order(1)
    @DisplayName("Create")
    public void addTest() {
        // Repository возвращает ID.
        Book book = testShellUtil.createBookList().get(0);
        Review review = new Review();
        review.setId("10");
        when(mockBookRepository.findById(any())).thenReturn(Optional.of(book));
        when(mockRepository.save(any())).thenReturn(review);
        // Команда (длинная)
        assertThat((String) shell.evaluate(COMMAND_ADD)).isEqualTo("Новый ID: 10");
        // До Repository дошло заданное значение.
        verify(mockRepository).save(new Review(book, "Отзыв"));
    }

    @Test
    @Order(2)
    @DisplayName("Update")
    public void updateTest() {
        Book book = testShellUtil.createBookList().get(0);
        Review review = new Review(book, "Отзыв");
        review.setId("1");
        when(mockRepository.findById(any())).thenReturn(Optional.of(review));
        when(mockRepository.existsById(any())).thenReturn(true);
        // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_UPDATE));
        // До Repository дошли значения из команды.
        verify(mockRepository).save(review);
    }

    @Test
    @Order(3)
    @DisplayName("Delete")
    public void deleteOkTest() {
        // Repository возвращает существующий объект.
        when(mockRepository.findById(any())).thenReturn(Optional.of(new Review(new Book(), "")));
        // Команда (длинная) удалить запись с ID = 100. Возвращает пользователю ОК.
        assertEquals("OK", shell.evaluate(COMMAND_DELETE));
        // До Repository дошло значение 100.
        verify(mockRepository).deleteById("100");
    }

}
