package ru.otus.job07.service;

import ru.otus.job07.model.Review;

import java.util.Optional;

public interface ReviewService {

    Optional<Review> getReviewById(Long reviewId);

    long addReview(long bookId, String opinion);

    void updateReview(Review review);

    void deleteReview(long reviewId);

}
