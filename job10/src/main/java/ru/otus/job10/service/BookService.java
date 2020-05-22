package ru.otus.job10.service;

import ru.otus.job10.model.Book;
import ru.otus.job10.model.dto.BookDto;
import ru.otus.job10.model.dto.BookInput;

import java.util.List;

public interface BookService {

    List<BookDto> getBookList();
    List<BookDto> getBookListByGenre(long genreId);
    List<BookDto> getBookListByAuthors(List<Long> authorsIds);

    BookDto getBookWithReview(long bookId);

    Book getBookById(long bookId);

    void addBook(BookInput bookInput);

    void updateBook(BookInput bookInput);

    void deleteBook(Long bookId);

}
