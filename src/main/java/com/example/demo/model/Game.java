package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // Legacy single-column storage kept for backward compatibility (reads existing DB rows)
    @Column(name = "genre")
    private String legacyGenre;

    // Changed: store multiple genres per game
    @ElementCollection
    @CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "genre")
    private List<String> genres = new ArrayList<>();

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

    // Lifecycle callbacks to migrate legacy single-column values into the collection and to keep them in sync
    @PostLoad
    private void populateGenresFromLegacy() {
        if ((this.genres == null || this.genres.isEmpty()) && this.legacyGenre != null && !this.legacyGenre.trim().isEmpty()) {
            setGenre(this.legacyGenre);
        }
    }

    @PrePersist
    @PreUpdate
    private void syncLegacyGenre() {
        // Keep the legacy column populated for compatibility with tools that expect it
        this.legacyGenre = getGenre();
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

    // New getters/setters for the list of genres
    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    // Backwards-compatible single-string accessor for existing code/templates.
    // Returns a single string with genres joined by a space.
    public String getGenre() {
        return String.join(" ", this.genres == null ? new ArrayList<>() : this.genres);
    }

    // Accept a single string (e.g. "Action RPG" or "Action, RPG") and split into individual genres.
    public void setGenre(String genre) {
        if (genre == null) {
            this.genres = new ArrayList<>();
            return;
        }
        // Split on commas, whitespace, or hyphens, trim each entry and filter out empties
        this.genres = Arrays.stream(genre.split("[,\\s-]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
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

    // Expose legacyGenre for compatibility checks
    public String getLegacyGenre() {
        return legacyGenre;
    }

}
