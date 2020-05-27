package ru.otus.job11.model.dto;

import ru.otus.job11.model.Genre;

public class GenreDto {

    private String genreId;
    private String genreName;

    public static GenreDto of(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setGenreId(genre.getId());
        genreDto.setGenreName(genre.getGenreName());
        return genreDto;
    }

    // generated getter & setters
    public String getGenreId() {
        return genreId;
    }
    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }
    public String getGenreName() {
        return genreName;
    }
    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
