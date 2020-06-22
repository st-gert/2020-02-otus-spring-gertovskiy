package ru.otus.job14.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Литературный жанр.
 */
@Document(collection = "genres")
public class GenreMongo {

    @Id
    private String id;

    private String genreName;

    @DBRef(lazy = true)
    private List<BookMongo> books = new ArrayList<>();

    public GenreMongo() {
    }

    public GenreMongo(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public String toString() {
        return id + ". " + genreName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreMongo genre = (GenreMongo) o;
        return Objects.equals(id, genre.id) &&
                Objects.equals(genreName, genre.genreName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genreName, books);
    }

    // generated getter & setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getGenreName() {
        return genreName;
    }
    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
    public List<BookMongo> getBooks() {
        return books;
    }
    public void setBooks(List<BookMongo> books) {
        this.books = books;
    }
}
