package ru.otus.job06.service;

import ru.otus.job06.model.Review;

import java.util.Optional;

public interface ReviewService {

    Optional<Review> getReviewById(Long reviewId);

    long addReview(long bookId, String opnion);

    void updateReview(Review review);

    void deleteReview(long reviewId);

}
