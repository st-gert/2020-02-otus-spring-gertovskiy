package ru.otus.job11.model.dto;

import ru.otus.job11.model.Author;
import ru.otus.job11.model.Book;
import ru.otus.job11.model.Review;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookDto {

    private String bookId;
    private String title;
    private String genreName;
    private String authorsString;

    private String genreId;
    private List<String> authorIds;

    private String firstAuthor;     // поле для сортировки

    private List<ReviewDto> reviews;

    public static BookDto of(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.bookId = book.getId();
        bookDto.title = book.getTitle();
        bookDto.genreName = book.getGenre().getGenreName();
        bookDto.firstAuthor = book.getAuthors().get(0).getLastName();
        bookDto.authorsString = book.getAuthors()
                .stream()
                .filter(a -> a.getLastName() != null)
                .sorted(Comparator.comparing(Author::getLastName).thenComparing(Author::getFirstName))
                .map(Author::getFullName)
                .collect(Collectors.joining(", "));
        bookDto.genreId = book.getGenre().getId();
        bookDto.authorIds = book.getAuthors()
                .stream()
                .map(Author::getId)
                .collect(Collectors.toList());
        return bookDto;
    }

    public static BookDto bookWithReviews(Book book) {
        BookDto bookDto = of(book);
        book.getReviews().sort(Comparator.comparing(Review::getId));
        bookDto.reviews = book.getReviews()
                .stream()
                .map(ReviewDto::of)
                .collect(Collectors.toList());
        return bookDto;
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
    public String getGenreName() {
        return genreName;
    }
    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
    public String getAuthorsString() {
        return authorsString;
    }
    public void setAuthorsString(String authorsString) {
        this.authorsString = authorsString;
    }
    public String getFirstAuthor() {
        return firstAuthor;
    }
    public void setFirstAuthor(String firstAuthor) {
        this.firstAuthor = firstAuthor;
    }
    public void setReviews(List<ReviewDto> reviews) {
        this.reviews = reviews;
    }
    public String getGenreId() {
        return genreId;
    }
    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }
    public List<String> getAuthorIds() {
        return authorIds;
    }
    public void setAuthorIds(List<String> authorIds) {
        this.authorIds = authorIds;
    }
}
