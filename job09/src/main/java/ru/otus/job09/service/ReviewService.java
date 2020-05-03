package ru.otus.job09.service;

import ru.otus.job09.model.Review;

public interface ReviewService {

    Review getReviewById(Long reviewId);

    void addReview(long bookId, String opinion);

    void updateReview(long reviewId, String opinion);

    void deleteReview(long reviewId);

}
