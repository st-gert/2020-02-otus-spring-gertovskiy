package ru.otus.job06.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;
import ru.otus.job06.model.Book;
import ru.otus.job06.model.Review;
import ru.otus.job06.repository.impl.ReviewRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тест Repository Управление отзывами на книги")
@DataJpaTest
@Import(ReviewRepositoryImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewRepositoryTest {

    @Autowired
    ReviewRepositoryImpl repository;

    @Autowired
    private TestEntityManager em;

    @Test
    @Order(0)
    @DisplayName("Добавление отзыва")
    void addReview() {
        Book book = em.find(Book.class, 1L);
        List<Review> reviews = book.getReviews();
        int n = CollectionUtils.isEmpty(reviews) ? 0 : reviews.size();
        book.addReview("Отзыв");
        repository.addReview(book);
        List<Review> UpdatedReviews = book.getReviews();
        assertAll(
                () -> assertNotNull(reviews)
                , () -> assertEquals(n + 1, reviews.size())
                , () -> assertEquals("Отзыв", reviews.get(n).getOpinion())
                , () -> assertTrue(reviews.get(n).getReviewId() > 0)
        );
    }

    @Test
    @DisplayName("Изменение отзыва")
    void updateReview() {
        Book book = em.find(Book.class, 1L);
        List<Review> reviews = book.getReviews();
        Review review = reviews.get(0);
        review.setOpinion("Отзыв-Отзыв");
        em.detach(book);
        repository.updateReview(review);
        Review updatedReview = em.find(Review.class, review.getReviewId());
//        Book updatedBook = em.find(Book.class, 1L);
        assertEquals("Отзыв-Отзыв", updatedReview.getOpinion());
    }

    @Test
    void deleteReview() {
        Book book = em.find(Book.class, 1L);
        int reviewSize = book.getReviews().size();
        long id = book.getReviews().get(0).getReviewId();
        em.detach(book);
        int n = repository.deleteReview(id);
        assertEquals(1, n);
        Book updatedBook = em.find(Book.class, 1L);
        assertEquals(reviewSize - 1, updatedBook.getReviews().size());
    }
}