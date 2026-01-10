package com.example.demo.controller;

import com.example.demo.model.Publisher;
import com.example.demo.service.PublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherRestController {

    private final PublisherService publisherService;

    public PublisherRestController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<Publisher> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @GetMapping("/{id}")
    public Publisher getPublisher(@PathVariable Long id) {
        // Vraća Publisher ili null ako ne postoji. Razmisliti o korištenju ResponseEntity za 404.
        return publisherService.getPublisherById(id);
    }

    @PostMapping
    public Publisher createPublisher(@RequestBody Publisher publisher) {
        // Jednostavan create bez DTO/validacije; u produkciji dodati validaciju polja.
        return publisherService.savePublisher(publisher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id, @RequestBody Publisher payload) {
        Publisher existing = publisherService.getPublisherById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setName(payload.getName());
        existing.setCountry(payload.getCountry());
        Publisher saved = publisherService.savePublisher(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        Publisher existing = publisherService.getPublisherById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        boolean deleted = publisherService.deleteById(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
