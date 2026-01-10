package com.example.demo.service;

import com.example.demo.model.Game;
import com.example.demo.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Servisni sloj koji enkapsulira pristup GameRepository-ju
    // Koristi se za buduću poslovnu logiku vezanu za igre.
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    // Briše igru po id-u; vraća false ako igra ne postoji
    // (omogućava REST kontroleru da vrati 404 umjesto 204)
    public boolean deleteById(Long id) {
        if (!gameRepository.existsById(id)) return false;
        gameRepository.deleteById(id);
        return true;
    }
}
