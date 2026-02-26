package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.request.RegisterRequestDTO;
import com.codecool.tttbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO req) throws SQLException {
        userService.register(req);

        return ResponseEntity.ok("Registered");
    }
}
