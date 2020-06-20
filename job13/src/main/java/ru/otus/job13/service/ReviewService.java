package ru.otus.job13.service;

import ru.otus.job13.model.dto.ReviewDto;

public interface ReviewService {

    ReviewDto getReviewById(Long reviewId);

    void addReview(long bookId, String opinion, String username);

    void updateReview(long reviewId, String opinion);

    void deleteReview(long reviewId);

}
