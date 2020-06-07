package ru.otus.job12.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.job12.exception.ApplDbConstraintException;
import ru.otus.job12.model.Genre;
import ru.otus.job12.service.GenreService;

import java.util.Comparator;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
public class GenreController {

    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping("/genre/list")
    public String listGenres(Model model) {
        List<Genre> genres = service.getGenreList();
        genres.sort(Comparator.comparing(Genre::getGenreName));
        model.addAttribute("genres", genres);
        return "/genre/list";
    }

    @GetMapping("/genre/add")
    public String addGenre(Model model) {
        model.addAttribute("genre", new Genre());
        return "/genre/edit";
    }

    @GetMapping("/genre/edit")
    public String editGenre(@RequestParam("id") Long genreId, Model model) {
        model.addAttribute("genre", service.getGenreById(genreId));
        return "/genre/edit";
    }

    @PostMapping("/genre/edit")
    public String saveGenre(Genre genre) {
        if (genre.getGenreId() == null) {
            service.addGenre(genre);
        } else {
            service.updateGenre(genre);
        }
        return "redirect:/genre/list";
    }

    @GetMapping("/genre/delete")
    public String deleteGenre(@RequestParam("id") Long genreId, Model model) {
        try {
            service.deleteGenre(genreId);
            return "redirect:/genre/list";
        } catch (ApplDbConstraintException e) {
            Genre genre = service.getGenreById(genreId);
            model.addAttribute("title", "Ошибка удаления");
            model.addAttribute("message",
                    "Жанр " + genre.getGenreName() + " удалить невозможно. Есть связанные с ним книги.");
            return "/message";
        }
    }

}
