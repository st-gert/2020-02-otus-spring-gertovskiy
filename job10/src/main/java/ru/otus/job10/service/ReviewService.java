package ru.otus.job10.service;

import ru.otus.job10.model.dto.ReviewDto;

public interface ReviewService {

    ReviewDto getReviewById(Long reviewId);

    void addReview(long bookId, String opinion);

    void updateReview(long reviewId, String opinion);

    void deleteReview(long reviewId);

}
