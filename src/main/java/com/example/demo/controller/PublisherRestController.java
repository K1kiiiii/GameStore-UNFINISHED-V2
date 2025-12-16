package com.example.demo.controller;

import com.example.demo.model.Publisher;
import com.example.demo.service.PublisherService;
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
}
