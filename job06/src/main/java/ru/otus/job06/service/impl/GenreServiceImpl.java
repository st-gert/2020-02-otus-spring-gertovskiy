package ru.otus.job06.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.otus.job06.exception.ApplDbConstraintException;
import ru.otus.job06.exception.ApplDbNoDataFoundtException;
import ru.otus.job06.model.Genre;
import ru.otus.job06.repository.GenreRepository;
import ru.otus.job06.service.GenreService;

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
        return repository.getGenreList();
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        return Optional.ofNullable( repository.getGenreById(id) );
    }

    @Override
    public long addGenre(Genre genre) {
        return repository.addGenre(genre);
    }

    @Override
    public void updateGenre(Genre genre) {
        Genre currentGenre = repository.getGenreById(genre.getGenreId());
        if (currentGenre == null) {
            throw new ApplDbNoDataFoundtException();
        }
        try {
            repository.updateGenre(genre);
        } catch (DataIntegrityViolationException e) {
            throw new ApplDbConstraintException();
        }
    }

    @Override
    public void deleteGenre(Long genreId) {
        Genre genre = repository.getGenreById(genreId);
        if (genre == null) {
            throw new ApplDbNoDataFoundtException();
        }
        try {
            repository.deleteGenre(genre);
        } catch (DataIntegrityViolationException e) {
            throw new ApplDbConstraintException();
        }
    }

}
