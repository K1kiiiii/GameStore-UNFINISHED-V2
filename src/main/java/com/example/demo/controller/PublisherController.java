package com.example.demo.controller;

import com.example.demo.model.Publisher;
import com.example.demo.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public String listPublishers(Model model) {
        model.addAttribute("publishers", publisherService.getAllPublishers());
        return "publishers";
    }

    @GetMapping("/new")
    public String newPublisherForm(Model model) {
        model.addAttribute("publisher", new Publisher());
        return "publisher-form";
    }

    @PostMapping
    public String createPublisher(@ModelAttribute Publisher publisher) {
        publisherService.savePublisher(publisher);
        return "redirect:/publishers";
    }

    @GetMapping("/edit/{id}")
    public String editPublisherForm(@PathVariable Long id, Model model) {
        model.addAttribute("publisher", publisherService.getPublisherById(id));
        return "publisher-form";
    }

    @PostMapping("/update/{id}")
    public String updatePublisher(@PathVariable Long id, @ModelAttribute Publisher publisher) {
        Publisher existing = publisherService.getPublisherById(id);
        if (existing == null) {
            return "redirect:/publishers";
        }
        existing.setName(publisher.getName());
        existing.setCountry(publisher.getCountry());
        publisherService.savePublisher(existing);
        return "redirect:/publishers";
    }
}

