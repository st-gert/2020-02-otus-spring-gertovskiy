package ru.otus.job10.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.job10.model.dto.BookDto;
import ru.otus.job10.service.impl.BookServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Сервис Управление книгами")
@DataJpaTest
@Import({BookServiceImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookReadTest {

    @Autowired
    BookServiceImpl bookService;

    @Autowired
    private TestEntityManager em;


    @Test
    @Order(0)
    @DisplayName("Получение списка всех книг и 1 запросом")
    void getBookList() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        List<BookDto> bookList = bookService.getBookList();
        assertThat(bookList)
                .hasSize(12)
                .allMatch(Objects::nonNull)
        ;
        long queriesCount = sessionFactory.getStatistics().getPrepareStatementCount();
        assertEquals(1L, queriesCount);
    }

    @Test
    @Order(1)
    @DisplayName("Список книг по жанру")
    void getBookListByGenre() {
        List<BookDto> list = bookService.getBookListByGenre(1L);
        assertThat(list)
                .isNotNull()
                .hasSize(4)
                .extracting(BookDto::getTitle)
                .contains("Сказка о Тройке", "Сто лет тому вперед")
                .doesNotContain("Комментарии к пройденному", "Азазель")
        ;
    }

    @Test
    @Order(2)
    @DisplayName("Список книг по авторам. Книг должно быть 3, а не 2, и не 5")
    void getBookListByAuthor() {
        List<BookDto> list = bookService.getBookListByAuthors(Arrays.asList(1L, 2L));   // 2 Стругацких
        assertThat(list)
                .isNotNull()
                .hasSize(3)
                .extracting(BookDto::getTitle)
                .containsOnly("Сказка о Тройке", "Комментарии к пройденному",
                        "Понедельник начинается в субботу")
        ;
        assertThat(list)
                .extracting(BookDto::getAuthorsString)
                .filteredOn(x -> x.contains(","))                    // 2 книги с 2 авторами
                .hasSize(2)
                .containsOnly("Аркадий Стругацкий, Борис Стругацкий")
        ;
        assertThat(list)
                .extracting(BookDto::getAuthorsString)
                .filteredOn(x -> !x.contains(","))                   // 1 книга с 1 автором
                .hasSize(1)
                .containsOnly("Борис Стругацкий")
        ;
    }

}