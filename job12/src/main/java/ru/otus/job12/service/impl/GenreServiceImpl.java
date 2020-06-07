package ru.otus.job12.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job12.exception.ApplDbNoDataFoundException;
import ru.otus.job12.model.Genre;
import ru.otus.job12.repository.GenreRepository;
import ru.otus.job12.service.GenreService;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository repository;

    public GenreServiceImpl(GenreRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Genre> getGenreList() {
        return repository.findAll();
    }

    @Override
    public Genre getGenreById(long id) {
        return repository.findById(id)
                .orElseThrow(ApplDbNoDataFoundException::new);
    }

    @Override
    @Transactional
    public void addGenre(Genre genre) {
        repository.save(genre);
    }

    @Override
    @Transactional
    public void updateGenre(Genre genre) {
        if (!repository.existsById(genre.getGenreId())) {
            throw new ApplDbNoDataFoundException();
        }
        repository.save(genre);
    }

    @Override
    @Transactional
    public void deleteGenre(Long genreId) {
        if (!repository.existsById(genreId)) {
            throw new ApplDbNoDataFoundException();
        }
        repository.deleteById(genreId);
    }

}
