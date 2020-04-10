package ru.otus.job06.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Книга.
 */
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(targetEntity = Genre.class)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToMany(targetEntity = Author.class, fetch = FetchType.EAGER)
    @JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Author> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Review> reviews = new ArrayList<>();

    public Book() {
    }

    public Book(Long bookId, String title, Genre genre, List<Author> authors) {
        this.bookId = bookId;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
    }

    public void addReview(String opinion) {
        reviews.add(new Review(null, this, opinion));
    }

    @Override
    public String toString() {
        String authorStr = CollectionUtils.isEmpty(authors) ? "? " : authors
                .stream()
                .sorted(Comparator.comparing(Author::getAuthorId))
                .map(Author::getFullName)
                .collect(Collectors.joining(", "));
        String reviewStr = CollectionUtils.isEmpty(reviews) ? "" : "\n" + reviews
                .stream()
                .sorted(Comparator.comparing(Review::getReviewId))
                .map(x -> "    - " + x.toString())
                .collect(Collectors.joining("\n"))
                ;
        return bookId + ". " + genre.getGenreName() + " - " + authorStr + ". " + title + reviewStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId) &&
                Objects.equals(title, book.title) &&
                Objects.equals(genre, book.genre) &&
                Objects.equals(authors, book.authors) &&
                Objects.equals(reviews, book.reviews);
    }

    // generated getter & setters
    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
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
