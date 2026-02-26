package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.request.RegisterRequest;
import com.codecool.tttbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
