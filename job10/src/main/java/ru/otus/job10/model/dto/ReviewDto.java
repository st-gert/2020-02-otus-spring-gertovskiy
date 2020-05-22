package ru.otus.job10.model.dto;

import ru.otus.job10.model.Review;

public class ReviewDto {

    private Long bookId;
    private Long reviewId;
    private String opinion;

    public static ReviewDto of(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.bookId = review.getBook().getBookId();
        reviewDto.reviewId = review.getReviewId();
        reviewDto.opinion = review.getOpinion();
        return reviewDto;
    }

    // generated getter & setters
    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public Long getReviewId() {
        return reviewId;
    }
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }
    public String getOpinion() {
        return opinion;
    }
    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
