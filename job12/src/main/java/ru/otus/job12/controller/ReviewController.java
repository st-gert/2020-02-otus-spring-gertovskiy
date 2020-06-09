package ru.otus.job12.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.job12.model.Book;
import ru.otus.job12.model.Review;
import ru.otus.job12.model.dto.BookDto;
import ru.otus.job12.service.BookService;
import ru.otus.job12.service.ReviewService;

@SuppressWarnings("SameReturnValue")
@Controller
public class ReviewController {

    private final ReviewService service;
    private final BookService bookService;

    public ReviewController(ReviewService service, BookService bookService) {
        this.service = service;
        this.bookService = bookService;
    }

    @GetMapping("/review/list")
    public String listReviewByBook(@RequestParam("id") Long bookId, Model model) {
        BookDto bookDto = bookService.getBookWithReview(bookId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("bookToString", bookDto.toString());
        model.addAttribute("reviews", bookDto.getReviews());
        return "/review/list";
    }

    @GetMapping("/review/add")
    public String addReview(@RequestParam("bookId") Long bookId, Model model) {
        Book book = new Book(bookId, null, null, null);
        Review review = new Review(null, book, null);
        model.addAttribute("review", review);
        return "/review/edit";
    }

    @GetMapping("/review/edit")
    public String editReview(@RequestParam("id") Long reviewId, Model model) {
        model.addAttribute("review", service.getReviewById(reviewId));
        return "/review/edit";
    }

    @PostMapping("/review/edit")
    public String saveReview(Long bookId, Long reviewId, String opinion, RedirectAttributes redirectAttributes) {
        if (reviewId == null) {
            service.addReview(bookId, opinion);
        } else {
            service.updateReview(reviewId, opinion);
        }
        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/review/list";
    }

    @GetMapping("/review/delete")
    public String deleteBook(@RequestParam("reviewId") Long reviewId, @RequestParam("bookId") Long bookId,
                             RedirectAttributes redirectAttributes) {
        service.deleteReview(reviewId);
        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/review/list";
    }

}
