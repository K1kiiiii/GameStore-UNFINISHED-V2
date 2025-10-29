package com.example.demo.config;

import com.example.demo.model.Game;
import com.example.demo.repository.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(GameRepository gameRepository) {
        return args -> {
            if (gameRepository.count() == 0) {

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

                Game g4 = new Game();
                g4.setTitle("Hades");
                g4.setGenre("Rogue-like");
                g4.setDeveloper("Supergiant Games");
                g4.setPrice(19.99);
                g4.setDescription("Battle your way out of the Underworld in this fast-paced action game.");
                g4.setImage("hades.jpg");
                g4.setRating(4);
                gameRepository.save(g4);

                Game g5 = new Game();
                g5.setTitle("Cyberpunk 2077");
                g5.setGenre("Open World RPG");
                g5.setDeveloper("CD Projekt Red");
                g5.setPrice(39.99);
                g5.setDescription("Experience life in the megalopolis of Night City as a mercenary outlaw.");
                g5.setImage("cyberpunk2077.jpg");
                g5.setRating(4);
                gameRepository.save(g5);

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

                Game g8 = new Game();
                g8.setTitle("God of War");
                g8.setGenre("Action-Adventure");
                g8.setDeveloper("Santa Monica Studio");
                g8.setPrice(49.99);
                g8.setDescription("Join Kratos and Atreus on an emotional journey through Norse mythology.");
                g8.setImage("godofwar.jpg");
                g8.setRating(5);
                gameRepository.save(g8);

                Game g9 = new Game();
                g9.setTitle("Minecraft");
                g9.setGenre("Sandbox");
                g9.setDeveloper("Mojang Studios");
                g9.setPrice(26.95);
                g9.setDescription("Build, explore, and survive in a blocky, procedurally generated world.");
                g9.setImage("minecraft.png");
                g9.setRating(5);
                gameRepository.save(g9);

                Game g10 = new Game();
                g10.setTitle("Terraria");
                g10.setGenre("Sandbox / Adventure");
                g10.setDeveloper("Re-Logic");
                g10.setPrice(9.99);
                g10.setDescription("Dig, fight, explore, and build in this 2D adventure game.");
                g10.setImage("terraria.jpg");
                g10.setRating(4);
                gameRepository.save(g10);

                System.out.println("✅ 10 sample games seeded successfully.");
            }
        };
    }
}
