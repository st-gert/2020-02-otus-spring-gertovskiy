package ru.otus.job05.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Книга.
 */
public class Book {

    private Long bookId;
    private String title;
    private Genre genre;
    private List<Author> authors;

    public Book(Long bookId, String title, Genre genre, List<Author> authors) {
        this.bookId = bookId;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
    }

    @Override
    public String toString() {
        String authorStr = authors.stream()
                .map(Author::getFullName)
                .collect(Collectors.joining(", "));
        return bookId + ". " + genre.getGenreName() + " - " + authorStr + ". " + title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId) &&
                Objects.equals(title, book.title) &&
                Objects.equals(genre, book.genre) &&
                Objects.equals(authors, book.authors);
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
}
