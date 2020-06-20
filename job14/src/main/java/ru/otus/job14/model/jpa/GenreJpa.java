package ru.otus.job14.model.jpa;

import ru.otus.job14.model.mongo.GenreMongo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

/**
 * Литературный жанр.
 */
@Entity
@Table(name = "genre")
public class GenreJpa {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String genreId;

    @Column(name = "genre_name", nullable = false, unique = true)
    private String genreName;

    @OneToMany(mappedBy = "genre")
    private List<BookJpa> books;

    public GenreJpa() {
    }

    public GenreJpa(GenreMongo genreMongo) {
        this.genreId = genreMongo.getId();
        this.genreName = genreMongo.getGenreName();
    }

    @Override
    public String toString() {
        return genreId + ". " + genreName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreJpa genre = (GenreJpa) o;
        return Objects.equals(genreId, genre.genreId) &&
                Objects.equals(genreName, genre.genreName);
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
    public List<BookJpa> getBooks() {
        return books;
    }
    public void setBooks(List<BookJpa> books) {
        this.books = books;
    }
}
