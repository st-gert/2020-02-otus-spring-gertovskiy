package ru.otus.job16.service;

import ru.otus.job16.model.Book;
import ru.otus.job16.model.dto.BookDto;
import ru.otus.job16.model.dto.BookInput;

import java.util.List;

public interface BookService {

    List<BookDto> getBookList();
    List<BookDto> getBookListByGenre(String genreId);
    List<BookDto> getBookListByAuthors(List<Long> authorsIds);

    BookDto getBookWithReview(long bookId);

    Book getBookById(long bookId);

    void addBook(BookInput bookInput);

    void updateBook(BookInput bookInput);

    void deleteBook(Long bookId);

}
