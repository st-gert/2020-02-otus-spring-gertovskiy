package ru.otus.job16.model.dto;

import ru.otus.job16.model.Genre;

public class GenreDto {

    private Long genreId;
    private String genreName;

    public static GenreDto of(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setGenreId(genre.getGenreId());
        genreDto.setGenreName(genre.getGenreName());
        return genreDto;
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
