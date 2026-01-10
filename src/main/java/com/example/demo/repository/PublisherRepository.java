package com.example.demo.repository;

import com.example.demo.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA repository za Publisher entitet; koristi se za CRUD operacije nad izdavačima.
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
