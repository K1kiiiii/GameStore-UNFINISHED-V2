package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;

    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/game/{gameId}")
    public List<Review> getReviewsForGame(@PathVariable Long gameId) {
        return reviewService.getReviewsForGame(gameId);
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }
}

