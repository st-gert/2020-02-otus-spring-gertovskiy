package ru.otus.job10.model.dto;

import ru.otus.job10.model.Author;

public class AuthorDto {

    private Long authorId;
    private String firstName;
    private String lastName;

    public static AuthorDto of(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setAuthorId(author.getAuthorId());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setLastName(author.getLastName());
        return authorDto;
    }

    // generated getter & setters
    public Long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(Long authorId) {
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
