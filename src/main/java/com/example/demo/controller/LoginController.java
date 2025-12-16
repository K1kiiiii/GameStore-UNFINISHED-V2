package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // Vraća Thymeleaf šablon za login (src/main/resources/templates/login.html)
        return "login"; // returns src/main/resources/templates/login.html
    }
}
