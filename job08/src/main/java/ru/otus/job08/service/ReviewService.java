package ru.otus.job08.service;

import ru.otus.job08.model.Review;

import java.util.Optional;

public interface ReviewService {

    Optional<Review> getReviewById(String reviewId);

    String addReview(String bookId, String opinion);

    void updateReview(String reviewId, String opinion);

    void deleteReview(String reviewId);

}
