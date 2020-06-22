package ru.otus.job14.model.jpa;

import ru.otus.job14.model.mongo.ReviewMongo;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class ReviewJpa {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String reviewId;

    @Column(name = "opinion", nullable = false)
    private String opinion;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookJpa book;

    public ReviewJpa() {
    }

    public ReviewJpa(ReviewMongo reviewMongo) {
        this.reviewId = reviewMongo.getId();
        this.opinion = reviewMongo.getOpinion();
        this.book = new BookJpa(reviewMongo.getBook());
    }

    @Override
    public String toString() {
        return opinion + " (" + reviewId + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewJpa review = (ReviewJpa) o;
        return Objects.equals(reviewId, review.reviewId) &&
                Objects.equals(opinion, review.opinion) &&
                Objects.equals(book, review.book);
    }

    // generated getter & setters
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
    public BookJpa getBook() {
        return book;
    }
    public void setBook(BookJpa book) {
        this.book = book;
    }
}
