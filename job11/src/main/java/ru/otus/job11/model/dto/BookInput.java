package ru.otus.job11.model.dto;

import java.util.List;

/**
 * DTO получения первичной информации для создания / корректировки книги.
 */
public class BookInput {

    private String bookId;
    private String title;
    private String genreId;
    private List<String> authorsIds;

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
    public String getGenreId() {
        return genreId;
    }
    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }
    public List<String> getAuthorsIds() {
        return authorsIds;
    }
    public void setAuthorsIds(List<String> authorsIds) {
        this.authorsIds = authorsIds;
    }
}
