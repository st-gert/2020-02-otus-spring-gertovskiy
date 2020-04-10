package ru.otus.job06.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.otus.job06.repository.GenreRepository;
import ru.otus.job06.model.Genre;
import ru.otus.job06.service.GenreController;

import java.util.List;

@Service
public class GenreControllerImpl implements GenreController {

    private final GenreRepository repository;
    private final ResultUtil resultUtil;

    public GenreControllerImpl(GenreRepository repository, ResultUtil resultUtil) {
        this.repository = repository;
        this.resultUtil = resultUtil;
    }

    @Override
    public Pair<List<Genre>, String> getGenreList() {
        try {
            return resultUtil.handleList(repository.getGenreList());
        } catch (Exception e) {
            return Pair.<List<Genre>, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public Pair<Long, String> addGenre(String genre) {
        try {
            return Pair.of(repository.addGenre(new Genre(null, genre)), null);
        } catch (Exception e) {
            return Pair.<Long, String>of(null, resultUtil.handleException(e));
        }
    }

    @Override
    public String updateGenre(Long genreId, String genre) {
        try {
            return resultUtil.handleInt(repository.updateGenre(new Genre(genreId, genre)));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

    @Override
    public String deleteGenre(Long genreId) {
        try {
            return resultUtil.handleInt(repository.deleteGenre(genreId));
        } catch (Exception e) {
            return resultUtil.handleException(e);
        }
    }

}
