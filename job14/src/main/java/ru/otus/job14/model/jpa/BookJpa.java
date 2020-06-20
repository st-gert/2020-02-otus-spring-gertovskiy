package ru.otus.job14.model.jpa;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.util.CollectionUtils;
import ru.otus.job14.model.mongo.BookMongo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Книга.
 */
@Entity
@Table(name = "book")
@NamedEntityGraph(name = "book-entity-graph",
        attributeNodes = {@NamedAttributeNode("reviews"), @NamedAttributeNode("genre")})
public class BookJpa {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private GenreJpa genre;

    @ManyToMany(targetEntity = AuthorJpa.class)
    @JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<AuthorJpa> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<ReviewJpa> reviews = new ArrayList<>();

    public BookJpa() {
    }

    public BookJpa(BookMongo bookMongo) {
        this.bookId = bookMongo.getId();
        this.title = bookMongo.getTitle();
        this.genre = new GenreJpa(bookMongo.getGenre());
        this.authors = bookMongo.getAuthors()
                .stream()
                .map(AuthorJpa::new)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String authorStr = CollectionUtils.isEmpty(authors) ? "? " : authors
                .stream()
                .map(AuthorJpa::getFullName)
                .collect(Collectors.joining(", "));
        String reviewStr = CollectionUtils.isEmpty(reviews) ? "" : "\n" + reviews
                .stream()
                .map(x -> "    - " + x.toString())
                .collect(Collectors.joining("\n"));
        return bookId + ". " + genre.getGenreName() + " - " + authorStr + ". " + title + reviewStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookJpa book = (BookJpa) o;
        return Objects.equals(bookId, book.bookId) &&
                Objects.equals(title, book.title) &&
                Objects.equals(genre, book.genre) &&
                Objects.equals(authors, book.authors) &&
                Objects.equals(reviews, book.reviews);
    }

    // generated getter & setters
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public GenreJpa getGenre() {
        return genre;
    }
    public void setGenre(GenreJpa genre) {
        this.genre = genre;
    }
    public List<AuthorJpa> getAuthors() {
        return authors;
    }
    public void setAuthors(List<AuthorJpa> authors) {
        this.authors = authors;
    }
    public List<ReviewJpa> getReviews() {
        return reviews;
    }
    public void setReviews(List<ReviewJpa> reviews) {
        this.reviews = reviews;
    }
}
