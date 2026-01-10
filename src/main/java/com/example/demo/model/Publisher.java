package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;

    // MappedBy znači da Game entitet posjeduje FK prema publisher-u.
    // Kolekcija može biti lazy, pa pristup iz view sloja van transakcije može baciti LazyInitializationException.
    @OneToMany(mappedBy = "publisher")
    private List<Game> games;

    // Publisher predstavlja izdavača igara; koristi se u vezi jedan-prema-više sa Game entitetom.
    public Publisher() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
