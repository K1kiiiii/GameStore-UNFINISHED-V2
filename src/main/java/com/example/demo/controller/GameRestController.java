package com.example.demo.controller;

import com.example.demo.model.Game;
import com.example.demo.model.Publisher;
import com.example.demo.service.GameService;
import com.example.demo.service.PublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
public class GameRestController {

    private final GameService gameService;
    private final PublisherService publisherService;

    public GameRestController(GameService gameService, PublisherService publisherService) {
        this.gameService = gameService;
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<Game> list() {
        return gameService.getAllGames();
    }

    @GetMapping("/search")
    public List<Game> search(@RequestParam(required = false) String q) {
        if (q == null || q.trim().isEmpty()) return new ArrayList<>();
        final String term = q.toLowerCase();
        return gameService.getAllGames().stream()
                .filter(g -> g.getTitle() != null && g.getTitle().toLowerCase().contains(term))
                .limit(10)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Game> create(@RequestBody Game game) {
        // Ensure ID null so JPA creates a new entity
        game.setId(null);

        // Normalize genres: if a genres list is present, trim and filter; otherwise rely on setGenre string-splitting
        if (game.getGenres() != null) {
            List<String> cleaned = game.getGenres().stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            game.setGenres(cleaned);
        } else {
            // ensure genres is not null (getGenre() joins empty list to empty string)
            String joined = game.getGenre();
            if (joined == null || joined.trim().isEmpty()) {
                game.setGenres(new ArrayList<>());
            } else {
                // setGenre will split the incoming string into the genres list
                game.setGenre(joined);
            }
        }

        Game saved = gameService.saveGame(game);
        // Return 201 Created and Location header
        return ResponseEntity.created(URI.create("/api/games/" + saved.getId())).body(saved);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAndCreate(@RequestParam(required = false) MultipartFile imageFile,
                                             @RequestParam String title,
                                             @RequestParam(required = false) String description,
                                             @RequestParam(required = false) String developer,
                                             @RequestParam(required = false) Double price,
                                             @RequestParam(required = false) Integer rating,
                                             @RequestParam(required = false) List<String> genres) {
        try {
            Game game = new Game();
            game.setId(null);
            game.setTitle(title);
            if (description != null) game.setDescription(description);
            game.setDeveloper(developer);
            if (price != null) game.setPrice(price);
            if (rating != null) game.setRating(rating);

            // Normalize genres list
            if (genres != null && !genres.isEmpty()) {
                List<String> cleaned = genres.stream().map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
                game.setGenres(cleaned);
            } else {
                game.setGenres(new ArrayList<>());
            }

            // Handle file upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String original = Path.of(imageFile.getOriginalFilename()).getFileName().toString();
                String safeName = original.replaceAll("[^A-Za-z0-9._-]", "_");
                Path imagesDir = Path.of("src", "main", "resources", "static", "images").toAbsolutePath();
                Files.createDirectories(imagesDir);
                Path target = imagesDir.resolve(safeName);
                // If filename exists, append a counter
                int i = 1;
                String base = safeName;
                while (Files.exists(target)) {
                    String name = base;
                    int dot = base.lastIndexOf('.');
                    if (dot > 0) {
                        String prefix = base.substring(0, dot);
                        String ext = base.substring(dot);
                        name = prefix + "-" + i + ext;
                    } else {
                        name = base + "-" + i;
                    }
                    target = imagesDir.resolve(name);
                    i++;
                }
                Files.copy(imageFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
                game.setImage(target.getFileName().toString());
            }

            Game saved = gameService.saveGame(game);
            return ResponseEntity.created(URI.create("/api/games/" + saved.getId())).body(saved);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to store uploaded file: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> update(@PathVariable Long id, @RequestBody Game payload) {
        Game existing = gameService.getGameById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        // update allowed fields
        existing.setTitle(payload.getTitle());
        // If payload provides explicit genres list, use it; otherwise use the single-string setter
        if (payload.getGenres() != null && !payload.getGenres().isEmpty()) {
            List<String> cleaned = payload.getGenres().stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            existing.setGenres(cleaned);
        } else {
            existing.setGenre(payload.getGenre());
        }
        existing.setDeveloper(payload.getDeveloper());
        existing.setDescription(payload.getDescription());
        existing.setImage(payload.getImage());
        existing.setPrice(payload.getPrice());
        existing.setRating(payload.getRating());
        // update publisher safely if payload contains id
        if (payload.getPublisher() != null && payload.getPublisher().getId() != null) {
            Publisher p = publisherService.getPublisherById(payload.getPublisher().getId());
            if (p != null) existing.setPublisher(p);
        }
        Game saved = gameService.saveGame(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = gameService.deleteById(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
