package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
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

    // CascadeType.ALL znači da će operacije nad Game (persist/remove) utjecati i na Review entitete.
    // Na primjer, brisanje Game-a obrisat će i povezane recenzije.
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Review> reviews;

    // Many-to-one veza prema Publisher; Game je vlasnik veze (sadrži FK `publisher_id`).
    // Paziti na lazy loading kada se pristupa iz view-a van transakcije.
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    // No-arg constructor
    public Game() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRating() {
        return rating;
    }

    // Ocjena igre; očekivani raspon 0-5. Razmotriti validaciju prilikom postavljanja.
    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    // Za finansijske vrijednosti preporučljivo koristiti BigDecimal, ovdje je pojednostavljeno.
    public void setPrice(double price) {
        this.price = price;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
