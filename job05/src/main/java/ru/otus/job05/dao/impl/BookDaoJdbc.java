package ru.otus.job05.dao.impl;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.otus.job05.dao.BookDao;
import ru.otus.job05.dao.ext.BookListResultSetExtractor;
import ru.otus.job05.dao.ext.BookResultSetExtractor;
import ru.otus.job05.model.Author;
import ru.otus.job05.model.Book;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final BookListResultSetExtractor listExtractor;
    private final ResultSetExtractor<List<Book>> oneBookExtractor = new BookResultSetExtractor();

    public BookDaoJdbc(NamedParameterJdbcOperations jdbcOperations, BookListResultSetExtractor listExtractor) {
        this.jdbcOperations = jdbcOperations;
        this.listExtractor = listExtractor;
    }

    @Override
    public List<Book> getBookList() {
        return jdbcOperations.query(
                "select b.id, b.title, b.genre_id, ab.author_id" +
                        " from book b" +
                        " join author_book ab on b.id = ab.book_id",
                listExtractor
        );
    }

    @Override
    public List<Book> getBookListByGenre(String genre) {
        return jdbcOperations.query(
                "select b.id, b.title, b.genre_id, ab.author_id" +
                        " from book b" +
                        " join author_book ab on b.id = ab.book_id" +
                        " where b.genre_id = (select g.id from genre g where lower(g.genre_name) = :genre )",
                new MapSqlParameterSource("genre", genre.toLowerCase()),
                listExtractor
        );
    }

    @Override
    public List<Book> getBookListByAuthor(String authorLastName) {
        return jdbcOperations.query(
                "select b.id, b.title, b.genre_id, ab.author_id" +
                        " from book b" +
                        " join author_book ab on b.id = ab.book_id" +
                        " where ab.author_id in (select a.id from author a where lower(a.last_name) = :author)",
                new MapSqlParameterSource("author", authorLastName.toLowerCase()),
                listExtractor
        );
    }

    @Override
    public Optional<Book> getBookById(Long bookId) {
        return Optional.ofNullable(
                CollectionUtils.firstElement(       // обеспечивает работу с пустым списком
                        jdbcOperations.query(
                                "select b.id, b.title, g.id genre_id, g.genre_name," +
                                        "     a.id author_id, a.first_name, a.last_name " +
                                        " from book b " +
                                        " join genre g on g.id = b.genre_id " +
                                        " join author_book ab on b.id = ab.book_id " +
                                        " join author a on a.id = ab.author_id " +
                                        " where b.id = :id",
                                new MapSqlParameterSource("id", bookId),
                                oneBookExtractor
                        )
                )
        );
    }

    /** @return ID нового объекта.  */
    @Override
    public Long addBook(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int n = jdbcOperations.update(
                "insert into book (title, genre_id) " +
                        " values (:title, :genre_id)",
                new MapSqlParameterSource()
                        .addValue("title", book.getTitle())
                        .addValue("genre_id", book.getGenre().getGenreId()),
                keyHolder,
                new String[]{"id"}
        );
        long book_id = keyHolder.getKey().longValue();
        // Добавляем связи с авторами
        for (Author author : book.getAuthors()) {
            jdbcOperations.update(
                    "insert into author_book (book_id, author_id) " +
                            " values (:book_id, :author_id)",
                    new MapSqlParameterSource()
                            .addValue("book_id", book_id)
                            .addValue("author_id", author.getAuthorId())
            );
        }
        return book_id;
    }

    /** @return новый объект, с новым ID.  */
    @Override
    public Optional<Book> updateBook(Book book) {
        // Удаляем книгу
        int n = deleteBook(book.getBookId());
        if (n == 0) {
            return Optional.empty();
        }
        // Добавляем книгу
        Long newId = addBook(book);
        book.setBookId(newId);
        return Optional.of(book);
    }

    /**
     * @return количество обработанных записей: 1 - OK, 0 - данные не найдены.
     */
    @Override
    public int deleteBook(Long bookId) {
        // Удаляем связи с авторами
        jdbcOperations.update(
                "delete from author_book ab where ab.book_id = :book_id",
                new MapSqlParameterSource("book_id", bookId)
        );
        // Удаляем книгу
        int n = jdbcOperations.update(
                "delete from book b where b.id = :book_id",
                new MapSqlParameterSource("book_id", bookId)
        );
        return n;
    }

}
