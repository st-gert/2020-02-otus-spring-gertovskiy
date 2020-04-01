package ru.otus.job05.dao;

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
     * @return количество обработанных записей либо признак нарушения d БД Constraints.
     *      > 0 - OK, ==0 - Данные не найдены, < 0 - Операция запрещена, нарушен Constraints.
     */
    int updateGenre(Genre genre);

    /**
     * @return количество обработанных записей либо признак нарушения d БД Constraints.
     *      > 0 - OK, ==0 - Данные не найдены, < 0 - Операция запрещена, нарушен Constraints.
     */
    int deleteGenre(Long genreId);

}


