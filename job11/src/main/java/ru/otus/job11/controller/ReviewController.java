package ru.otus.job11.controller;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.otus.job11.model.Book;
import ru.otus.job11.model.Review;
import ru.otus.job11.model.dto.BookDto;
import ru.otus.job11.model.dto.ReviewDto;
import ru.otus.job11.repository.BookRepository;
import ru.otus.job11.repository.ReviewRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class ReviewController {

    private final ReviewRepository repository;
    private final BookRepository bookRepository;

    public ReviewController(ReviewRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    @Bean
    public RouterFunction<ServerResponse> reviewRoutes() {
        return route()
                .GET("/api/review/book/{bookId}", this::findBookWithReviews)
                .GET("/api/review/{id}", this::findById)
                .POST("/api/review", this::insert)
                .PUT("/api/review", this::save)
                .DELETE("/api/review/{id}", this::delete)
                .build();
    }

    private Mono<ServerResponse> findBookWithReviews(ServerRequest request) {
        String bookId = request.pathVariable("bookId");
        return bookRepository.findById(bookId)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга ID " + bookId + " не найдена")
                ))
                .map(BookDto::bookWithReviews)
                .flatMap(bookDto -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(bookDto)
                )
                ;
    }

    private Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв ID " + id + " не найден")
                ))
                .map(ReviewDto::of)
                .flatMap(review -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(review)
                )
                ;
    }

    private Mono<ServerResponse> insert(ServerRequest request) {
        return request
                // получаем 2 ID и текст отзыва. ID отзыва пустой
                .bodyToMono(ReviewDto.class)
                .filter(reviewDto -> reviewDto.getReviewId() == null)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                                "Невозможно добавить отзыв с имеющимся ID")))
                // получаем книгу по ID
                .map(reviewDto -> Pair.of(reviewDto, bookRepository.findById(reviewDto.getBookId())))
                .filter(pair -> !pair.getRight().equals(Mono.empty()))
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга не найдена")
                ))
                // формируем отзыв и записываем (книга внутри отзыва)
                .flatMap(pair -> pair.getRight().map(book -> {
                    Review review = new Review(book, pair.getLeft().getOpinion());
                    Mono<Review> newReview = repository.insert(review);
                    return Pair.of(newReview, book);
                }))
                // книге добавляем отзыв и сохраняем (двунаправленная связь)
                .flatMap(pair -> pair.getLeft().map(review -> {
                    Book book = pair.getRight();
                    book.getReviews().add(review);
                    Mono<Book> savedBook = bookRepository.save(book);
                    return Pair.of(review, savedBook);
                }))
                // книга не сохранится без какого-либо нового шага с ней
                .flatMap(pair -> pair.getRight().map(book -> pair.getLeft()))
                // финиш (машина железная, от такого действа с ума не сойдет)
                .map(ReviewDto::of)
                .flatMap(review -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(review)
                )
                ;
    }

    private Mono<ServerResponse> save(ServerRequest request) {
        return request
                .bodyToMono(ReviewDto.class)
                .map(review -> Pair.of(review, repository.findById(review.getReviewId())))
                .filter(pair -> !pair.getRight().equals(Mono.empty()))
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв не найден")))
                .flatMap(pair -> pair.getRight().flatMap(review -> {
                            review.setOpinion(pair.getLeft().getOpinion());
                            return repository.save(review);
                        })
                )
                .map(ReviewDto::of)
                .flatMap(review -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(review)
                )
                ;
    }

    private Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return repository.existsById(id)
                .filter(x -> x)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв ID " + id + " не найден")))
                .flatMap(x -> repository.deleteById(id))
                .flatMap(v -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue("")
                )
                ;
    }

}
