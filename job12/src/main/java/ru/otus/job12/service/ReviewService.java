package ru.otus.job12.service;

import ru.otus.job12.model.Review;

public interface ReviewService {

    Review getReviewById(Long reviewId);

    void addReview(long bookId, String opinion);

    void updateReview(long reviewId, String opinion);

    void deleteReview(long reviewId);

}
