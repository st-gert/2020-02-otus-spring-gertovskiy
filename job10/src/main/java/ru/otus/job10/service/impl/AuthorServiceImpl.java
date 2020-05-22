package ru.otus.job10.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job10.exception.ApplDbNoDataFoundException;
import ru.otus.job10.model.Author;
import ru.otus.job10.model.dto.AuthorDto;
import ru.otus.job10.repository.AuthorRepository;
import ru.otus.job10.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AuthorDto> getAuthorList() {
        List<Author> list = repository.findAll();
        return list
                .stream()
                .map(AuthorDto::of)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public AuthorDto getAuthorById(long id) {
        return AuthorDto.of(
                repository.findById(id)
                        .orElseThrow(ApplDbNoDataFoundException::new)
        );
    }

    @Override
    @Transactional
    public void addAuthor(Author author) {
        repository.save(author);
    }

    @Override
    @Transactional
    public void updateAuthor(Author author) {
        if (!repository.existsById(author.getAuthorId())) {
            throw new ApplDbNoDataFoundException();
        }
        repository.save(author);
    }

    @Override
    @Transactional
    public void deleteAuthor(long authorId) {
        if (!repository.existsById(authorId)) {
            throw new ApplDbNoDataFoundException();
        }
        repository.deleteById(authorId);
    }

}
