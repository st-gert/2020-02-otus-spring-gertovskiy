package ru.otus.job05.dao;

import ru.otus.job05.model.Author;

import java.util.List;
import java.util.Optional;

/**
 * DAO Авторы.
 */
public interface AuthorDao {

    List<Author> getAuthorList();
    Optional<Author> getAuthorById(Long id);
    Optional<Author> getAuthorByName(Author author);

    /** @return ID нового объекта.  */
    Long addAuthor(Author author);

    /**
     * @return количество обработанных записей либо признак нарушения d БД Constraints.
     *      > 0 - OK, ==0 - Данные не найдены, < 0 - Операция запрещена, нарушен Constraints.
     */
    int updateAuthor(Author author);

    /**
     * @return количество обработанных записей либо признак нарушения d БД Constraints.
     *      > 0 - OK, ==0 - Данные не найдены, < 0 - Операция запрещена, нарушен Constraints.
     */
    int deleteAuthor(Long authorId);

}
