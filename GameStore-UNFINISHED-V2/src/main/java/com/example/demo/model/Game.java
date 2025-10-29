package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private String developer;
    private String description;
    private String image;
    private int rating;
    private double price;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
