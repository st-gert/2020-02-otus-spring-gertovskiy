package ru.otus.job11.model.dto;

import ru.otus.job11.model.Author;

public class AuthorDto {

    private String authorId;
    private String firstName;
    private String lastName;

    public static AuthorDto of(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setAuthorId(author.getId());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setLastName(author.getLastName());
        return authorDto;
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
}
