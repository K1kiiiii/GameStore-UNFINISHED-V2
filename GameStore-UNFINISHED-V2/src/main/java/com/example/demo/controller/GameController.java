package com.example.demo.controller;

import com.example.demo.model.Game;
import com.example.demo.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
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
        return "game-details";
    }


    @GetMapping("/games")
    public String gamesPage(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "games"; // Matches games.html
    }

}
