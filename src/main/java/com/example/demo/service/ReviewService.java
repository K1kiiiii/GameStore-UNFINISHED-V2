package com.example.demo.service;

import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Servisni sloj za recenzije: mjesto za validaciju ocjena i poslovnu logiku.
    public List<Review> getReviewsForGame(Long gameId) {
        // Vraća sve recenzije za zadani gameId koristeći custom repository metodu.
        return reviewRepository.findByGameId(gameId);
    }

    public Review saveReview(Review review) {
        // Ovdje bi se mogla dodati validacija (npr. opseg ratinga) prije spremanja.
        return reviewRepository.save(review);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
