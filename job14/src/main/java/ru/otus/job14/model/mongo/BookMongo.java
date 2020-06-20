package ru.otus.job14.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Книга.
 */
@Document(collection = "books")
public class BookMongo {

    @Id
    private String id;

    private String title;

    @DBRef
    private GenreMongo genre;

    @DBRef
    private List<AuthorMongo> authors;

    @DBRef(lazy = true)
    private List<ReviewMongo> reviews = new ArrayList<>();

    public BookMongo() {
    }

    public BookMongo(String title, GenreMongo genre, List<AuthorMongo> authors) {
        this.title = title;
        this.genre = genre;
        this.authors = authors;
    }

    @Override
    public String toString() {
        String authorStr = CollectionUtils.isEmpty(authors) ? "? " : authors
                .stream()
                .map(AuthorMongo::getFullName)
                .collect(Collectors.joining(", "));
        String reviewStr = CollectionUtils.isEmpty(reviews) ? "" : "\n" + reviews
                .stream()
                .map(x -> "    - " + x.toString())
                .collect(Collectors.joining("\n"))
                ;
        return id + ". " + genre.getGenreName() + " - " + authorStr + ". " + title + reviewStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookMongo book = (BookMongo) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(title, book.title) &&
                Objects.equals(genre, book.genre) &&
                Objects.equals(authors, book.authors) &&
                Objects.equals(reviews, book.reviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, genre, authors, reviews);
    }

    // generated getter & setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public GenreMongo getGenre() {
        return genre;
    }
    public void setGenre(GenreMongo genre) {
        this.genre = genre;
    }
    public List<AuthorMongo> getAuthors() {
        return authors;
    }
    public void setAuthors(List<AuthorMongo> authors) {
        this.authors = authors;
    }
    public List<ReviewMongo> getReviews() {
        return reviews;
    }
    public void setReviews(List<ReviewMongo> reviews) {
        this.reviews = reviews;
    }
}
