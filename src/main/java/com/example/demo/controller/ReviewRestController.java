package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;

    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // REST API za čitanje i kreiranje recenzija.
    // Očekuje JSON sa poljima Review entiteta; nema dodatne validacije ovdje.
    @GetMapping("/game/{gameId}")
    public List<Review> getReviewsForGame(@PathVariable Long gameId) {
        // Vraća listu svih recenzija povezanih sa datom igrom (može vratiti praznu listu)
        return reviewService.getReviewsForGame(gameId);
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        // Napravi novu recenziju; u produkciji dodati provjere i validaciju inputa
        return reviewService.saveReview(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review payload) {
        Review existing = reviewService.getReviewById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        // Update allowed fields
        existing.setAuthor(payload.getAuthor());
        existing.setContent(payload.getContent());
        existing.setRating(payload.getRating());
        // Note: relationship to Game is left as-is to avoid accidental reassignment; if payload
        // contains a game id and you want to support re-linking, enhance service accordingly.
        Review saved = reviewService.saveReview(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        Review existing = reviewService.getReviewById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
