package com.example.demo.controller;

import com.example.demo.model.Game;
import com.example.demo.model.Publisher;
import com.example.demo.service.GameService;
import com.example.demo.service.PublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @PostMapping
    public ResponseEntity<Game> create(@RequestBody Game game) {
        // Ensure ID null so JPA creates a new entity
        // Postavljanje id-na null sprječava slučajno ažuriranje postojećeg entiteta ako klijent pošalje id
        game.setId(null);
        Game saved = gameService.saveGame(game);
        // Vraćamo 201 Created i Location header sa lokacijom novog resursa
        return ResponseEntity.created(URI.create("/api/games/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> update(@PathVariable Long id, @RequestBody Game payload) {
        Game existing = gameService.getGameById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        // update allowed fields
        existing.setTitle(payload.getTitle());
        existing.setGenre(payload.getGenre());
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
