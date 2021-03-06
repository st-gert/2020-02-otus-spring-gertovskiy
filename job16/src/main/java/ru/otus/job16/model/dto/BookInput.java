package ru.otus.job16.model.dto;

import java.util.List;

/**
 * DTO получения первичной информации для создания / корректировки книги.
 */
public class BookInput {

    private Long bookId;
    private String title;
    private String genreName;
    private List<Long> authorsIds;

    // generated getter
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
    public List<Long> getAuthorsIds() {
        return authorsIds;
    }
    public void setAuthorsIds(List<Long> authorsIds) {
        this.authorsIds = authorsIds;
    }
    public String getGenreName() {
        return genreName;
    }
    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
