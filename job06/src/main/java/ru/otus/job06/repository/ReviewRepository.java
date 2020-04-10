package ru.otus.job06.repository;

import ru.otus.job06.model.Book;
import ru.otus.job06.model.Review;

import java.util.Optional;

/**
 * Repository Отзывы на книги.
 */
public interface ReviewRepository {

    Optional<Review> getReviewById(Long reviewId);

    long addReview(Book book);

    void updateReview(Review review);

    int deleteReview(Long reviewId);

}
