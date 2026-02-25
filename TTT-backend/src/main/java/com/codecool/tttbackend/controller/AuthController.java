package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.request.LoginRequest;
import com.codecool.tttbackend.controller.dto.request.RegisterRequest;
import com.codecool.tttbackend.controller.dto.response.TokenResponse;
import com.codecool.tttbackend.service.JwtService;
import com.codecool.tttbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService){

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
        userService.register(req.email(), req.username(), req.password(), req.birthDate());
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        try {
            System.out.println("Login attempt: " + req.username());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password())
            );
            String token = jwtService.generateToken(req.username());
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/test")
    public String test() {
        return "ok";
    }
}