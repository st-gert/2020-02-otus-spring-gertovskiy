package ru.otus.job06.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.job06.model.Book;
import ru.otus.job06.repository.impl.BookRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Проверка количесва запросов к БД")
@DataJpaTest
@Import(BookRepositoryImpl.class)
public class BookListStatisticsTest {
    private static final long EXPECTED_QUERIES_COUNT = 2L;

    @Autowired
    BookRepositoryImpl repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Получение списка книг 2-мя запросами")
    void getBookList() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        List<Book> bookList = repository.getBookList();
        bookList.stream()
                .map(Book::toString)
                .forEach(x -> {});
        long queriesCount = sessionFactory.getStatistics().getPrepareStatementCount();
        System.out.println(queriesCount);
        assertEquals(EXPECTED_QUERIES_COUNT, queriesCount);
    }

}
