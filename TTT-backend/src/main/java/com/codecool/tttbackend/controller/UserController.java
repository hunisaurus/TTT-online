package com.codecool.tttbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        return Map.of("status", "OK", "message", "Backend is running on Java 25!");
    }
}
