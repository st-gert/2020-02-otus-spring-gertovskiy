package ru.otus.job14.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * Отзыв на книгу.
 */
@Document(collection = "reviews")
public class ReviewMongo {

    @Id
    private String id;

    private String opinion;

    @DBRef
    private BookMongo book;

    public ReviewMongo() {
    }

    public ReviewMongo(BookMongo book, String opinion) {
        this.book = book;
        this.opinion = opinion;
    }

    @Override
    public String toString() {
        return opinion + " (" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewMongo review = (ReviewMongo) o;
        return Objects.equals(id, review.id) &&
                Objects.equals(opinion, review.opinion) &&
                Objects.equals(book, review.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, opinion, book);
    }

    // generated getter & setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOpinion() {
        return opinion;
    }
    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
    public BookMongo getBook() {
        return book;
    }
    public void setBook(BookMongo book) {
        this.book = book;
    }
}
