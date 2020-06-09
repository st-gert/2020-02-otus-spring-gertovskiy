package ru.otus.job12.service;

import ru.otus.job12.model.Book;
import ru.otus.job12.model.dto.BookDto;

import java.util.List;

public interface BookService {

    List<BookDto> getBookList();
    List<BookDto> getBookListByGenre(long genreId);
    List<BookDto> getBookListByAuthors(List<Long> authorsIds);

    BookDto getBookWithReview(long bookId);

    Book getBookById(long bookId);

    void addBook(String title, Long genreId, List<Long> authorsIds);

    void updateBook(long bookId, String title, Long genreId, List<Long> authorsIds);

    void deleteBook(Long bookId);

}
