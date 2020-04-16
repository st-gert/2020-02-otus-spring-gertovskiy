package ru.otus.job06.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.job06.model.Book;
import ru.otus.job06.repository.impl.AuthorRepositoryImpl;
import ru.otus.job06.repository.impl.BookRepositoryImpl;
import ru.otus.job06.repository.impl.GenreRepositoryImpl;
import ru.otus.job06.service.impl.BookServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Проверка количесва запросов к БД")
@DataJpaTest
@Import({BookServiceImpl.class, BookRepositoryImpl.class, AuthorRepositoryImpl.class, GenreRepositoryImpl.class})
public class BookListStatisticsTest {

    @Autowired
    BookServiceImpl service;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Получение списка всех книг")
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
