package ru.otus.job16.service;

import ru.otus.job16.model.dto.ReviewDto;

public interface ReviewService {

    ReviewDto getReviewById(Long reviewId);

    long addReview(long bookId, String opinion);

    void updateReview(long reviewId, String opinion);

    void deleteReview(long reviewId);

}
