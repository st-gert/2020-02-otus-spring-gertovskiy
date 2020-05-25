package ru.otus.job11.model.dto;

import ru.otus.job11.model.Review;

public class ReviewDto {

    private String bookId;
    private String reviewId;
    private String opinion;

    public static ReviewDto of(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.bookId = review.getBook().getId();
        reviewDto.reviewId = review.getId();
        reviewDto.opinion = review.getOpinion();
        return reviewDto;
    }

    // generated getter & setters
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getReviewId() {
        return reviewId;
    }
    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }
    public String getOpinion() {
        return opinion;
    }
    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
