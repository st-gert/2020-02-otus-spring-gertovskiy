package ru.otus.job12.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.job12.exception.ApplDbConstraintException;
import ru.otus.job12.model.Author;
import ru.otus.job12.service.AuthorService;

import java.util.Comparator;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping("/author/list")
    public String listAuthors(Model model) {
        List<Author> authors = service.getAuthorList();
        authors.sort(Comparator.comparing(Author::getLastName).thenComparing(Author::getFirstName));
        model.addAttribute("authors", authors);
        return "/author/list";
    }

    @GetMapping("/author/add")
    public String addAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "/author/edit";
    }

    @GetMapping("/author/edit")
    public String editAuthor(@RequestParam("id") Long authorId, Model model) {
        model.addAttribute("author", service.getAuthorById(authorId));
        return "/author/edit";
    }

    @PostMapping("/author/edit")
    public String saveAuthor(Author author) {
        if (author.getAuthorId() == null) {
            service.addAuthor(author);
        } else {
            service.updateAuthor(author);
        }
        return "redirect:/author/list";
    }

    @GetMapping("/author/delete")
    public String deleteAuthor(@RequestParam("id") Long authorId, Model model) {
        try {
            service.deleteAuthor(authorId);
            return "redirect:/author/list";
        } catch (ApplDbConstraintException e) {
            Author author = service.getAuthorById(authorId);
            model.addAttribute("title", "Ошибка удаления");
            model.addAttribute("message",
                    "Автора " + author.getFullName() + " удалить невозможно. Есть связанные с ним книги.");
            return "/message";
        }
    }

}
