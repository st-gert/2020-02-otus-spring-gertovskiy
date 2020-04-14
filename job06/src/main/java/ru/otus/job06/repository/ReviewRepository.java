package ru.otus.job06.repository;

import ru.otus.job06.model.Review;

/**
 * Repository Отзывы на книги.
 */
public interface ReviewRepository {

    Review getReviewById(long reviewId);

    long addReview(Review review);

    void updateReview(Review review);

    void deleteReview(Review review);

}
