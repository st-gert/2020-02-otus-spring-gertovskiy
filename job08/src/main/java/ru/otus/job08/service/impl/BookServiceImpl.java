package ru.otus.job08.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.job08.exception.ApplDbNoDataFoundException;
import ru.otus.job08.model.Author;
import ru.otus.job08.model.Book;
import ru.otus.job08.model.Genre;
import ru.otus.job08.model.Review;
import ru.otus.job08.repository.AuthorRepository;
import ru.otus.job08.repository.BookRepository;
import ru.otus.job08.repository.GenreRepository;
import ru.otus.job08.repository.ReviewRepository;
import ru.otus.job08.service.BookService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;

    public BookServiceImpl(BookRepository repository, AuthorRepository authorRepository,
                           GenreRepository genreRepository, ReviewRepository reviewRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Book> getBookList() {
        return repository.findAll()
                .stream()
                .distinct()
                .peek(this::sortAuthorsAndReviews)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<Book> getBookListByGenre(String genreName) {
        Genre genre = genreRepository.findByGenreNameIgnoreCase(genreName);
        if (genre == null || genre.getBooks().isEmpty()) {
            throw new ApplDbNoDataFoundException();
        }
        List<Book> books = genre.getBooks();
        books.forEach(this::sortAuthorsAndReviews);
        return books;
    }

    @Override
    public List<Book> getBookListByAuthor(String authorLastName) {
        List<Author> authors = authorRepository.findAll();
        List<Book> books = authors
                .stream()
                .filter(x -> x.getLastName().equalsIgnoreCase(authorLastName))
                .flatMap(x -> x.getBooks().stream())
                .distinct()
                .peek(this::sortAuthorsAndReviews)
                .collect(Collectors.toList());
        if (books.isEmpty()) {
            throw new ApplDbNoDataFoundException();
        }
        return books;
    }

    @Override
    public String addBook(Book book) {
        obtainAuthorAndGenreId(book);
        return repository.save(book).getId();
    }

    @Override
    public void updateBook(Book book) {
        if (!repository.existsById(book.getId())) {
            throw new ApplDbNoDataFoundException();
        }
        obtainAuthorAndGenreId(book);
        repository.save(book);
    }

    @Override
    public void deleteBook(String bookId) {
        Optional<Book> optionalBook = repository.findById(bookId);
        // Удаляем связанные отзывы
        List<Review> reviews = optionalBook
                .map(Book::getReviews)
                .orElseThrow(ApplDbNoDataFoundException::new);
        reviews.forEach(reviewRepository::delete);
        // Удаляем связи
        Book book = optionalBook.get(); // выше проверено, что книга существует
        Genre genre = book.getGenre();
        genre.getBooks().remove(book);
        genreRepository.save(genre);
        book.getAuthors().forEach(author -> {
            author.getBooks().remove(book);
            authorRepository.save(author);
        });
        // Теперь можно саму книгу
        repository.deleteById(bookId);
    }

    private void obtainAuthorAndGenreId(Book book) {
        Genre genre = book.getGenre();
        Genre foundGenre = genreRepository.findByGenreNameIgnoreCase(genre.getGenreName());
        if (foundGenre != null) {
            genre.setId(foundGenre.getId());
        } else {
            genre.setId(genreRepository.save(genre).getId());
        }
        for (Author author : book.getAuthors()) {
            Author foundAuthor = authorRepository.getByFirstNameIgnoreCaseAndLastNameIgnoreCase(
                    author.getFirstName(), author.getLastName());
            if (foundAuthor != null) {
                author.setId(foundAuthor.getId());
            } else {
                author.setId(authorRepository.save(author).getId());
            }
        }
    }

    private void sortAuthorsAndReviews(Book book) {
        book.getAuthors().sort(Comparator
                .comparing(Author::getLastName)
                .thenComparing(Author::getFirstName)
        );
        book.getReviews().sort(Comparator
                .comparing(Review::getId)
        );
    }

}
