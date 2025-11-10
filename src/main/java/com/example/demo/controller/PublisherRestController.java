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
        return publisherService.getPublisherById(id);
    }

    @PostMapping
    public Publisher createPublisher(@RequestBody Publisher publisher) {
        return publisherService.savePublisher(publisher);
    }
}

