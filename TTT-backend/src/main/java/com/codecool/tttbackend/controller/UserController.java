package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.request.LoginRequest;
import com.codecool.tttbackend.controller.dto.request.RegisterRequest;
import com.codecool.tttbackend.controller.dto.response.TokenResponse;
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
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) throws SQLException {
        userService.register(req);

        return ResponseEntity.ok("Registered");
    }
}
