package ru.otus.job08.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.job08.exception.ApplDbConstraintException;
import ru.otus.job08.exception.ApplDbNoDataFoundException;
import ru.otus.job08.model.Genre;
import ru.otus.job08.repository.GenreRepository;
import ru.otus.job08.service.GenreService;

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
    public Optional<Genre> getGenreById(String id) {
        return repository.findById(id);
    }

    @Override
    public String addGenre(Genre genre) {
        Genre addedGenre = repository.save(genre);
        return addedGenre.getId();
    }

    @Override
    public void updateGenre(String genreId, String genreName) {
        Optional<Genre> optionalGenre = repository.findById(genreId);
        Genre genre = optionalGenre
                .orElseThrow(ApplDbNoDataFoundException::new);
        genre.setGenreName(genreName);
        repository.save(genre);
    }

    @Override
    public void deleteGenre(String genreId) {
        Optional<Genre> optionalGenre = repository.findById(genreId);
        int cntBook = optionalGenre
                .map(a -> a.getBooks().size())
                .orElseThrow(ApplDbNoDataFoundException::new);
        if (cntBook > 0) {
            throw new ApplDbConstraintException();
        }
        repository.deleteById(genreId);
    }

}
