package ru.otus.job07.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job07.exception.ApplDbNoDataFoundException;
import ru.otus.job07.model.Genre;
import ru.otus.job07.repository.GenreRepository;
import ru.otus.job07.service.GenreService;

import java.util.List;
import java.util.Optional;

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
    public Optional<Genre> getGenreById(long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public long addGenre(Genre genre) {
        Genre addedGenre = repository.save(genre);
        return addedGenre.getGenreId();
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
