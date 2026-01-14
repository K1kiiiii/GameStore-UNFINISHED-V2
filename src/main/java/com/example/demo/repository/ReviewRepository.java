package com.example.demo.repository;

import com.example.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Pronalaženje svih recenzija vezanih za igru po njenom id-u.
    // Metoda vraća praznu listu ako nema rezultata; može se proširiti paginacijom/ sortiranjem.
    List<Review> findByGameId(Long gameId);
}
