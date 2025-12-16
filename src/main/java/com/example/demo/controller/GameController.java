package com.example.demo.controller;

import com.example.demo.model.Game;
import com.example.demo.model.Review;
import com.example.demo.service.GameService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class GameController {

    private final GameService gameService;
    private final ReviewService reviewService;
    private final PublisherService publisherService;

    public GameController(GameService gameService, ReviewService reviewService, PublisherService publisherService) {
        this.gameService = gameService;
        this.reviewService = reviewService;
        this.publisherService = publisherService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "index";
    }

    @GetMapping("/games/{id}")
    public String gameDetails(@PathVariable Long id, Model model) {
        Game game = gameService.getGameById(id);
        if (game == null) {
            return "redirect:/games"; // fallback if not found
        }
        model.addAttribute("game", game);
        model.addAttribute("reviews", reviewService.getReviewsForGame(id));
        model.addAttribute("newReview", new Review());
        // compute average rating and provide as 1-decimal formatted string
        // Ako nema recenzija, vraća se 0.0 kao prosjek; ovo je UI-friendly fallback.
        double avg = reviewService.getReviewsForGame(id)
                .stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        String avgFormatted = String.format("%.1f", avg);
        model.addAttribute("avgRating", avgFormatted);
        return "game-details";
    }

    @PostMapping("/games/{id}/reviews")
    public String addReview(@PathVariable Long id, @ModelAttribute Review newReview) {
        Game game = gameService.getGameById(id);
        if (game == null) {
            return "redirect:/games";
        }
        // Osiguravamo da se radi o novom entitetu: postavljamo id na null kako bi JPA persistovao novi zapis
        newReview.setId(null);
        newReview.setGame(game);
        reviewService.saveReview(newReview);
        return "redirect:/games/" + id;
    }

    @PostMapping("/games/{gameId}/reviews/update/{reviewId}")
    public String updateReview(@PathVariable Long gameId, @PathVariable Long reviewId, @ModelAttribute Review review) {
        Review existing = reviewService.getReviewById(reviewId);
        if (existing == null) {
            return "redirect:/games/" + gameId;
        }
        existing.setAuthor(review.getAuthor());
        existing.setContent(review.getContent());
        existing.setRating(review.getRating());
        reviewService.saveReview(existing);
        return "redirect:/games/" + gameId;
    }


    @GetMapping("/games")
    public String gamesPage(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "games"; // Matches games.html
    }

}
