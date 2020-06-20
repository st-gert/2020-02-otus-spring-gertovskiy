package ru.otus.job13.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.job13.model.Author;
import ru.otus.job13.model.Genre;
import ru.otus.job13.model.dto.BookDto;
import ru.otus.job13.service.AuthorService;
import ru.otus.job13.service.BookService;
import ru.otus.job13.service.GenreService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Controller
public class BookListController {

    private final BookService service;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookListController(BookService service, AuthorService authorService, GenreService genreService) {
        this.service = service;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    // Список всех книг
    @GetMapping("/book/list")
    public String listBooks(Model model) {
        List<BookDto> bookDtoList = service.getBookList();
        return outputBookList(bookDtoList, model);
    }


    private String outputBookList(List<BookDto> bookDtoList, Model model) {
        bookDtoList.sort(Comparator
                .comparing(BookDto::getGenreName)
                .thenComparing(BookDto::getFirstAuthor)
                .thenComparing(BookDto::getTitle));
        model.addAttribute("books", bookDtoList);
        return "/book/list";
    }

    // 2 метода список книг по жанру
    @GetMapping("/book/list/genre")
    public String getGenreForBookList(Model model) {
        List<Genre> genres = genreService.getGenreList();
        genres.sort(Comparator.comparing(Genre::getGenreName));
        model.addAttribute("genres", genres);
        return "/book/genre";
    }

    @PostMapping("/book/list/genre")
    public String listBooksByGenre(Long id, Model model) {
        List<BookDto> bookDtoList = service.getBookListByGenre(id);
        return outputBookList(bookDtoList, model);
    }

    // 2 метода список книг по авторам
    @GetMapping("/book/list/authors")
    public String getAuthorsForBookList(Model model) {
        List<Author> authors = authorService.getAuthorList();
        authors.sort(Comparator.comparing(Author::getLastName).thenComparing(Author::getFirstName));
        model.addAttribute("authors", authors);
        return "/book/authors";
    }

    @PostMapping("/book/list/authors")
    public String listBooksByAuthors(@RequestBody MultiValueMap<String, String> valueMap, Model model) {
        List<Long> authorsId = valueMap.get("selectedId")
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<BookDto> bookDtoList = service.getBookListByAuthors(authorsId);
        return outputBookList(bookDtoList, model);
    }

}
