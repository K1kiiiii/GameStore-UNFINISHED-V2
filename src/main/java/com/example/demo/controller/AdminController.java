package com.example.demo.controller;

import com.example.demo.model.Game;
import com.example.demo.model.Review;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final GameRepository gameRepository;
    private final ReviewRepository reviewRepository;

    public AdminController(GameRepository gameRepository, ReviewRepository reviewRepository) {
        this.gameRepository = gameRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/seed")
    public ResponseEntity<String> seedDatabase() {
        // Clear existing data
        reviewRepository.deleteAll();
        gameRepository.deleteAll();

        // Seed games
        Game g1 = new Game();
        g1.setTitle("The Witcher 3: Wild Hunt");
        g1.setGenre("RPG");
        g1.setDeveloper("CD Projekt Red");
        g1.setPrice(29.99);
        g1.setDescription("Play as Geralt of Rivia in an open world full of choices and monsters.");
        g1.setImage("witcher3.webp");
        g1.setRating(5);
        gameRepository.save(g1);

        Game g2 = new Game();
        g2.setTitle("Red Dead Redemption 2");
        g2.setGenre("Action-Adventure");
        g2.setDeveloper("Rockstar Games");
        g2.setPrice(59.99);
        g2.setDescription("Epic Western adventure in a vast open world full of life and detail.");
        g2.setImage("rdr2.jpg");
        g2.setRating(5);
        gameRepository.save(g2);

        Game g3 = new Game();
        g3.setTitle("Elden Ring");
        g3.setGenre("Action RPG");
        g3.setDeveloper("FromSoftware");
        g3.setPrice(49.99);
        g3.setDescription("A dark fantasy epic crafted by FromSoftware and George R. R. Martin.");
        g3.setImage("eldenring.jpg");
        g3.setRating(5);
        gameRepository.save(g3);

        Game g6 = new Game();
        g6.setTitle("Stardew Valley");
        g6.setGenre("Simulation");
        g6.setDeveloper("ConcernedApe");
        g6.setPrice(14.99);
        g6.setDescription("Build your farm, grow crops, raise animals, and form relationships.");
        g6.setImage("stardewvalley.png");
        g6.setRating(5);
        gameRepository.save(g6);

        Game g7 = new Game();
        g7.setTitle("Hollow Knight");
        g7.setGenre("Metroidvania");
        g7.setDeveloper("Team Cherry");
        g7.setPrice(14.99);
        g7.setDescription("Descend into the beautifully haunted world of Hallownest.");
        g7.setImage("hollowknight.png");
        g7.setRating(5);
        gameRepository.save(g7);

        Game g9 = new Game();
        g9.setTitle("Minecraft");
        g9.setGenre("Sandbox");
        g9.setDeveloper("Mojang Studios");
        g9.setPrice(26.95);
        g9.setDescription("Build, explore, and survive in a blocky, procedurally generated world.");
        g9.setImage("minecraft.png");
        g9.setRating(5);
        gameRepository.save(g9);

        // Seed sample reviews
        Review r1 = new Review();
        r1.setAuthor("Alex");
        r1.setContent("Incredible storytelling and open world.");
        r1.setRating(5);
        r1.setGame(g1);
        reviewRepository.save(r1);

        Review r2 = new Review();
        r2.setAuthor("Sam");
        r2.setContent("Beautiful visuals, a bit slow at times.");
        r2.setRating(4);
        r2.setGame(g2);
        reviewRepository.save(r2);

        Review r3 = new Review();
        r3.setAuthor("Jordan");
        r3.setContent("Challenging and rewarding combat.");
        r3.setRating(5);
        r3.setGame(g3);
        reviewRepository.save(r3);

        Review r4 = new Review();
        r4.setAuthor("Casey");
        r4.setContent("Addictive gameplay loop.");
        r4.setRating(5);
        r4.setGame(g6);
        reviewRepository.save(r4);

        Review r5 = new Review();
        r5.setAuthor("Taylor");
        r5.setContent("Great for creative play.");
        r5.setRating(5);
        r5.setGame(g9);
        reviewRepository.save(r5);

        Review r6 = new Review();
        r6.setAuthor("Morgan");
        r6.setContent("Classic indie charm.");
        r6.setRating(4);
        r6.setGame(g7);
        reviewRepository.save(r6);

        return ResponseEntity.ok("Database reseeded with sample games and reviews.");
    }
}

