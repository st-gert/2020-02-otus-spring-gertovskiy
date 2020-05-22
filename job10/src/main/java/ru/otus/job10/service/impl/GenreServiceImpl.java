package ru.otus.job10.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job10.exception.ApplDbNoDataFoundException;
import ru.otus.job10.model.Genre;
import ru.otus.job10.model.dto.GenreDto;
import ru.otus.job10.repository.GenreRepository;
import ru.otus.job10.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository repository;

    public GenreServiceImpl(GenreRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<GenreDto> getGenreList() {
        List<Genre> list = repository.findAll();
        return list
                .stream()
                .map(GenreDto::of)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public GenreDto getGenreById(long id) {
        return GenreDto.of(
                repository.findById(id)
                        .orElseThrow(ApplDbNoDataFoundException::new)
        );
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
