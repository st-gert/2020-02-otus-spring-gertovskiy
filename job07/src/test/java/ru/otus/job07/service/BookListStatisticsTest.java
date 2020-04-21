package ru.otus.job07.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.job07.model.Book;
import ru.otus.job07.service.impl.BookServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Проверка количесва запросов к БД")
@DataJpaTest
@Import({BookServiceImpl.class})
public class BookListStatisticsTest {

    @Autowired
    BookServiceImpl service;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Получение списка всех книг 2 запросами")
    void getBookList() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        List<Book> bookList = service.getBookList();
        bookList.stream()
                .map(Book::toString)
                .forEach(x -> {});
        long queriesCount = sessionFactory.getStatistics().getPrepareStatementCount();
        assertEquals(2L, queriesCount);
    }

}
