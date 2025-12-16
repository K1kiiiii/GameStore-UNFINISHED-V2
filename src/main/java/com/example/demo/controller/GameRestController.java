package com.example.demo.controller;

import com.example.demo.model.Game;
import com.example.demo.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameRestController {

    private final GameService gameService;

    public GameRestController(GameService gameService) {
        this.gameService = gameService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = gameService.deleteById(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
