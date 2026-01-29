package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.LoginRequest;
import com.codecool.tttbackend.controller.dto.RegisterRequest;
import com.codecool.tttbackend.controller.dto.TokenResponse;
import com.codecool.tttbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        return Map.of("status", "OK", "message", "Backend is running on Java 25!");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) throws SQLException {
        userService.register(req.email(), req.username(), req.password());

        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) throws SQLException {
        String token = userService.login(req.username(), req.password());

        return ResponseEntity.ok(new TokenResponse(token));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        userService.logout(token);

        return ResponseEntity.ok("Logged out");
    }
}
