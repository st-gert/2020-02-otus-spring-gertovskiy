package ru.otus.job13.model.dto;

import ru.otus.job13.model.Author;
import ru.otus.job13.model.Book;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookDto {

    private final Long bookId;
    private final String title;
    private final String genreName;
    private final String authorsString;

    private final String firstAuthor;     // поле для сортировки

    private List<ReviewDto> reviews;

    public BookDto(Book book) {
        this.bookId = book.getBookId();
        this.title = book.getTitle();
        this.genreName = book.getGenre().getGenreName();
        this.firstAuthor = book.getAuthors().get(0).getLastName();
        this.authorsString = book.getAuthors()
                .stream()
                .sorted(Comparator.comparing(Author::getLastName).thenComparing(Author::getFirstName))
                .map(Author::getFullName)
                .collect(Collectors.joining(", "));
    }

    public BookDto(Book book, boolean withReviews) {
        this(book);
        if (withReviews) {
            this.reviews = book.getReviews()
                    .stream()
                    .map(ReviewDto::new)
                    .sorted(Comparator.comparing(ReviewDto::getReviewId))
                    .collect(Collectors.toList())
            ;
        }
    }

    public List<ReviewDto> getReviews() {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        return reviews;
    }

    @Override
    public String toString() {
        return genreName + " - " + authorsString + ". " + title;
    }

    // generated getter
    public Long getBookId() {
        return bookId;
    }
    public String getTitle() {
        return title;
    }
    public String getGenreName() {
        return genreName;
    }
    public String getAuthorsString() {
        return authorsString;
    }
    public String getFirstAuthor() {
        return firstAuthor;
    }
}
