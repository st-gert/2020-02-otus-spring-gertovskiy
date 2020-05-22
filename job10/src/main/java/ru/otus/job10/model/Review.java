package ru.otus.job10.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Отзыв на книгу.
 */
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long reviewId;

    @Column(name = "opinion", nullable = false)
    private String opinion;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Review() {
    }

    public Review(Long reviewId, Book book, String opinion) {
        this.reviewId = reviewId;
        this.book = book;
        this.opinion = opinion;
    }

    @Override
    public String toString() {
        return opinion + " (" + reviewId + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(reviewId, review.reviewId) &&
                Objects.equals(opinion, review.opinion) &&
                Objects.equals(book, review.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, opinion, book);
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
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
}
