package ru.otus.job08.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.job08.exception.ApplDbConstraintException;
import ru.otus.job08.exception.ApplDbNoDataFoundException;
import ru.otus.job08.model.Author;
import ru.otus.job08.repository.AuthorRepository;
import ru.otus.job08.service.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Author> getAuthorList() {
        return repository.findAll();
    }

    @Override
    public Optional<Author> getAuthorById(String id) {
        return repository.findById(id);
    }

    @Override
    public String addAuthor(Author author) {
        Author addedAuthor = repository.save(author);
        return addedAuthor.getId();
    }

    @Override
    public void updateAuthor(Author author) {
        if (!repository.existsById(author.getId())) {
            throw new ApplDbNoDataFoundException();
        }
        repository.save(author);
    }

    @Override
    public void deleteAuthor(String authorId) {
        Optional<Author> optionalAuthor = repository.findById(authorId);
        int cntBook = optionalAuthor
                .map(a -> a.getBooks().size())
                .orElseThrow(ApplDbNoDataFoundException::new);
        if (cntBook > 0) {
            throw new ApplDbConstraintException();
        }
        repository.deleteById(authorId);
    }

}
