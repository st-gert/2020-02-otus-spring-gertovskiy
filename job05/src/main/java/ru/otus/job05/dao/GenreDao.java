package ru.otus.job05.dao;

import ru.otus.job05.exception.ApplDbConstraintException;
import ru.otus.job05.model.Genre;

import java.util.List;
import java.util.Optional;

/**
 * DAO Литературные жанры.
 */
public interface GenreDao {

    List<Genre> getGenreList();

    Optional<Genre> getGenreById(Long id);

    Optional<Genre> getGenreByName(String genreName);

    /**
     * @return ID нового объекта.
     */
    long addGenre(Genre genre);

    /**
     * @param genre измененный объект
     * @return количество обработанных записей: 1 - OK, 0 - данные не найдены
     * @throws ApplDbConstraintException операция запрещена, нарушен Constraints
     */
    int updateGenre(Genre genre) throws ApplDbConstraintException;

    /**
     * @param genreId ID объекта
     * @return количество обработанных записей: 1 - OK, 0 - данные не найдены
     * @throws ApplDbConstraintException операция запрещена, нарушен Constraints
     */
    int deleteGenre(Long genreId) throws ApplDbConstraintException;

}


