package ru.otus.job14.model.jpa;

import ru.otus.job14.model.mongo.AuthorMongo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

/**
 * Автор.
 */
@Entity
@Table(name = "author")
public class AuthorJpa {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String authorId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToMany(mappedBy = "authors")
    private List<BookJpa> books;

    public AuthorJpa() {
    }

    public AuthorJpa(AuthorMongo authorMongo) {
        this.authorId = authorMongo.getId();
        this.firstName = authorMongo.getFirstName();
        this.lastName = authorMongo.getLastName();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return authorId +  ". " + firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorJpa author = (AuthorJpa) o;
        return Objects.equals(authorId, author.authorId) &&
                Objects.equals(firstName, author.firstName) &&
                Objects.equals(lastName, author.lastName);
    }

    // generated getter & setters
    public String getAuthorId() {
        return authorId;
    }
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public List<BookJpa> getBooks() {
        return books;
    }
    public void setBooks(List<BookJpa> books) {
        this.books = books;
    }
}
