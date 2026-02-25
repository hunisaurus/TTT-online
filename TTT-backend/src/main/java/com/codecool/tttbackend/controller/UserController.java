package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.request.LoginRequestDTO;
import com.codecool.tttbackend.controller.dto.request.RegisterRequestDTO;
import com.codecool.tttbackend.controller.dto.response.TokenResponseDTO;
import com.codecool.tttbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO req) throws SQLException {
        userService.register(req.email(), req.username(), req.password(), req.birthDate());

        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        userService.logout(token);

        return ResponseEntity.ok("Logged out");
    }
}
