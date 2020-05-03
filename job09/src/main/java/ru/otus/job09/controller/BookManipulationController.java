package ru.otus.job09.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.job09.model.Author;
import ru.otus.job09.model.Book;
import ru.otus.job09.model.Genre;
import ru.otus.job09.service.AuthorService;
import ru.otus.job09.service.BookService;
import ru.otus.job09.service.GenreService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Controller
public class BookManipulationController {

    private final BookService service;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookManipulationController(BookService service, AuthorService authorService, GenreService genreService) {
        this.service = service;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    // Добавление книги
    @GetMapping("/book/add")
    public String addBook(Model model) {
        Book book = new Book(null, "", new Genre(), new ArrayList<>());
        return edit(book, model);
    }

    // Корректировка книги
    @GetMapping("/book/edit")
    public String editBook(@RequestParam("id") Long bookId, Model model) {
        return edit(service.getBookById(bookId), model);
    }

    private String edit(Book book, Model model) {
        List<Genre> genres = genreService.getGenreList();
        List<Author> authors = authorService.getAuthorList();
        genres.sort(Comparator.comparing(Genre::getGenreName));
        authors.sort(Comparator.comparing(Author::getLastName).thenComparing(Author::getFirstName));
        model.addAttribute("book", book);
        model.addAttribute("allGenres", genres);
        model.addAttribute("allAuthors", authors);
        return "/book/edit";
    }

    // Сохранить после добавления и корректировки
    @PostMapping("/book/save")
    public String saveBook(@RequestBody MultiValueMap<String, String> valueMap) {
        String bookId = valueMap.getFirst("bookId");
        String title = valueMap.getFirst("title");
        String genreId = valueMap.getFirst("genreId");
        List<String> authorsIds = valueMap.get("authorId");
        Long longGenreId = Long.parseLong(genreId);
        List<Long> longAuthorsIds = authorsIds
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList())
                ;
        if (StringUtils.isBlank(bookId)) {
            service.addBook(title, longGenreId, longAuthorsIds);
        } else {
            service.updateBook(Long.parseLong(bookId), title, longGenreId, longAuthorsIds);
        }
        return "redirect:/book/list";
    }

    // Удаление книги
    @GetMapping("/book/delete")
    public String deleteBook(@RequestParam("id") Long bookId) {
        service.deleteBook(bookId);
        return "redirect:/book/list";
    }

}
