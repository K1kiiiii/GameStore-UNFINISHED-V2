package com.example.demo.repository;

import com.example.demo.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Game entity;
 * inherits basic CRUD and paging/sorting methods.
 */
public interface GameRepository extends JpaRepository<Game, Long> {
}
