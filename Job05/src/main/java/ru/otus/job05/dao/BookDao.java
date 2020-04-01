package ru.otus.job05.dao;

import ru.otus.job05.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * DAO Книги.
 */
public interface BookDao {

    List<Book> getBookList();
    List<Book> getBookListByGenre(String genre);
    List<Book> getBookListByAuthor(String authorLastName);
    Optional<Book> getBookById(Long bookId);

    /**
     * @return ID нового объекта.
     */
    Long addBook(Book book);

    /**
     * @return новый объект (возможно с новым ID, зависит от реализации).
     */
    Optional<Book> updateBook(Book book);

    /**
     * @return количество обработанных записей либо признак нарушения d БД Constraints.
     *      > 0 - OK, ==0 - Данные не найдены, < 0 - Операция запрещена, нарушен Constraints.
     */
    int deleteBook(Long bookId);

}
