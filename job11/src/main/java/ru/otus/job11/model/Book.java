package ru.otus.job11.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;
import ru.otus.job11.model.Author;
import ru.otus.job11.model.Genre;
import ru.otus.job11.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Книга.
 */
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String title;

    @DBRef
    private Genre genre;

    @DBRef
    private List<Author> authors;

    @DBRef(lazy = true)
    private List<Review> reviews = new ArrayList<>();

    public Book() {
    }

    public Book(String title, Genre genre, List<Author> authors) {
        this.title = title;
        this.genre = genre;
        this.authors = authors;
    }

    public void update(String title, String genreId, List<String> authorIds) {
        this.title = title;
        this.genre = new Genre();
        genre.setId(genreId);
        this.authors = authorIds
                .stream()
                .map(id -> {Author a = new Author(); a.setId(id); return a;})
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String authorStr = CollectionUtils.isEmpty(authors) ? "? " : authors
                .stream()
                .map(Author::getFullName)
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
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(title, book.title) &&
                Objects.equals(genre, book.genre) &&
                Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, genre,authors);
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
    public Genre getGenre() {
        return genre;
    }
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    public List<Author> getAuthors() {
        return authors;
    }
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
