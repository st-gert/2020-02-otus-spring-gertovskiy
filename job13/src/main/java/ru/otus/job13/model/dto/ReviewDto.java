package ru.otus.job13.model.dto;

import ru.otus.job13.model.Review;

/**
 * Отзыв на книгу.
 */
public class ReviewDto {

    private Long reviewId;
    private String opinion;
    private Long bookId;
    private String username;

    public ReviewDto() {
    }

    public ReviewDto(Review review) {
        this.reviewId = review.getId();
        this.opinion = review.getOpinion();
        this.bookId = review.getBook().getBookId();
        this.username = review.getUser().getUsername();
    }

    // generated getter & setters
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
    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
