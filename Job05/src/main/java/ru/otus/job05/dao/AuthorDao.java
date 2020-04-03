package ru.otus.job05.dao;

import ru.otus.job05.exception.ApplDbConstraintException;
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
     * @param author измененный объект
     * @return количество обработанных записей: 1 - OK, 0 - данные не найдены
     * @throws ApplDbConstraintException операция запрещена, нарушен Constraints
     */
    int updateAuthor(Author author) throws ApplDbConstraintException;

    /**
     * @param authorId ID объекта
     * @return количество обработанных записей: 1 - OK, 0 - данные не найдены
     * @throws ApplDbConstraintException операция запрещена, нарушен Constraints
     */
    int deleteAuthor(Long authorId) throws ApplDbConstraintException;

}
