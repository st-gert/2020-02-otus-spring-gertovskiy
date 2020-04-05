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
     * @return количество обработанных записей: 1 - OK, 0 - данные не найдены.
     */
    int deleteBook(Long bookId);

}
