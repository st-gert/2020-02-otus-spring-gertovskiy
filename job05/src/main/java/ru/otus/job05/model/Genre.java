package ru.otus.job05.model;

import java.util.Objects;

/**
 * Литературный жанр.
 */
public class Genre {

    private Long genreId;
    private String genreName;

    public Genre(Long genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }

    @Override
    public String toString() {
        return genreId + ". " + genreName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(genreId, genre.genreId) &&
                Objects.equals(genreName, genre.genreName);
    }

    // generated getter & setters
    public Long getGenreId() {
        return genreId;
    }
    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }
    public String getGenreName() {
        return genreName;
    }
    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
